package eatyourbeets.ui.animator.characterSelection;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.screens.charSelect.CharacterOption;
import eatyourbeets.resources.animator.misc.AnimatorLoadout;
import eatyourbeets.resources.animator.misc.AnimatorLoadoutData;
import eatyourbeets.ui.GUIElement;
import eatyourbeets.ui.controls.GUI_Button;
import eatyourbeets.ui.controls.GUI_Dropdown;
import eatyourbeets.ui.controls.GUI_Image;
import eatyourbeets.ui.controls.GUI_Label;
import eatyourbeets.ui.hitboxes.AdvancedHitbox;
import eatyourbeets.ui.hitboxes.RelativeHitbox;
import eatyourbeets.utilities.EYBFontHelper;
import eatyourbeets.utilities.JUtils;

public class AnimatorBaseStatEditor extends GUIElement
{
    public enum Type
    {
        Gold,
        HP;

        public Texture GetIcon()
        {
            return this == Gold ? ImageMaster.TP_GOLD : ImageMaster.TP_HP;
        }

        public Color GetColor()
        {
            return this == Gold ? Settings.GOLD_COLOR : Settings.RED_TEXT_COLOR;
        }

        public void SetAmount(AnimatorLoadoutData data, int amount)
        {
            if (this == Gold)
            {
                data.Gold = amount;
            }
            else
            {
                data.HP = amount;
            }
        }

        public int GetBaseValue()
        {
            return this == Gold ? AnimatorLoadout.BASE_GOLD : AnimatorLoadout.BASE_HP;
        }

        public int GetStep()
        {
            return this == Gold ? AnimatorLoadout.GOLD_STEP : AnimatorLoadout.HP_STEP;
        }

        public int GetAmount(AnimatorLoadoutData data)
        {
            return this == Gold ? data.Gold : data.HP;
        }

        public String GetText(AnimatorLoadoutData data)
        {
            return (this == Gold ? CharacterOption.TEXT[5] : CharacterOption.TEXT[4]) + GetAmount(data);
        }
    }

    public AnimatorBaseStatEditor.Type type;
    public AnimatorLoadoutData data;

    protected static final float ICON_SIZE = 64f * Settings.scale;
    protected boolean interactable;
    protected Hitbox hb;
    protected GUI_Image image;
    protected GUI_Label label;
    protected GUI_Button decrease_button;
    protected GUI_Button increase_button;
    protected GUI_Dropdown<Integer> valueDropdown;
    protected AnimatorLoadoutEditor editor;

    public AnimatorBaseStatEditor(AnimatorBaseStatEditor.Type type, float cX, float cY, AnimatorLoadoutEditor editor)
    {
        this.type = type;
        this.hb = new AdvancedHitbox(0, 0, ICON_SIZE * 2.5f, ICON_SIZE).SetPosition(cX, cY);
        this.editor = editor;

        final float w = hb.width;
        final float h = hb.height;

        image = new GUI_Image(type.GetIcon(), new RelativeHitbox(hb, ICON_SIZE, ICON_SIZE, (ICON_SIZE * 0.5f), h * 0.5f, false));
        label = new GUI_Label(FontHelper.tipHeaderFont, new RelativeHitbox(hb, w - ICON_SIZE, h, (w + ICON_SIZE) * 0.5f, h * 0.5f, false))
        .SetAlignment(0.5f, 0f, false)
        .SetColor(type.GetColor());

        decrease_button = new GUI_Button(ImageMaster.CF_LEFT_ARROW, new RelativeHitbox(hb, ICON_SIZE, ICON_SIZE, -(ICON_SIZE * 0.5f), (h * 0.35f), false))
        .SetOnClick(this::Decrease)
        .SetText(null);

        increase_button = new GUI_Button(ImageMaster.CF_RIGHT_ARROW, new RelativeHitbox(hb, ICON_SIZE, ICON_SIZE, w + (ICON_SIZE * 0.5f), (h * 0.35f), false))
        .SetOnClick(this::Increase)
        .SetText(null);

        valueDropdown = new GUI_Dropdown<Integer>(new RelativeHitbox(hb, 0.5f, 0.75f, 0.5f, -0.1f))
                .SetFontForButton(EYBFontHelper.CardTitleFont_Small, 1f)
                .SetOnOpenOrClose(isOpen -> {
                    editor.activeEditor = isOpen ? this : null;
                })
                .SetOnChange(value -> {
                    if (value.size() > 0) {
                        Set(value.get(0));
                    }
                })
                .SetCanAutosizeButton(true)
                .SetLabelFunctionForButton(null, value -> {
                    if (value.isEmpty()) {
                        return Settings.CREAM_COLOR;
                    }
                    int first = value.get(0);
                    return first == 0 ? Settings.CREAM_COLOR : first < 0 ? Settings.RED_TEXT_COLOR : Settings.GREEN_TEXT_COLOR;
                }, false)
                .SetItems(JUtils.RangeArray(AnimatorLoadout.MAX_STEP * -1, AnimatorLoadout.MAX_STEP));
    }

    public AnimatorBaseStatEditor SetEstimatedValue(int value)
    {
        valueDropdown.SetSelection(value, true);

        return this;
    }

    public AnimatorBaseStatEditor SetInteractable(boolean interactable)
    {
        this.interactable = interactable;

        return this;
    }
    
    @Override
    public void Update()
    {
        hb.update();
        image.Update();
        label.SetText(type.GetText(data)).Update();
        decrease_button.SetInteractable(interactable && CanDecrease()).Update();
        increase_button.SetInteractable(interactable && CanIncrease()).Update();
        valueDropdown.TryUpdate();
    }

    @Override
    public void Render(SpriteBatch sb)
    {
        image.Render(sb);
        label.Render(sb);
        decrease_button.Render(sb);
        increase_button.Render(sb);
        valueDropdown.TryRender(sb);
    }

    public void SetLoadout(AnimatorLoadoutData data)
    {
        this.data = data;
    }

    public boolean CanDecrease()
    {
        return valueDropdown.GetCurrentIndex() > 0;
    }

    public void Decrease()
    {
        valueDropdown.SetSelectionIndices(new int[]{valueDropdown.GetCurrentIndex() - 1}, true);
    }

    public boolean CanIncrease()
    {
        return valueDropdown.GetCurrentIndex() < valueDropdown.rows.size() - 1;
    }

    public void Increase()
    {
        valueDropdown.SetSelectionIndices(new int[]{valueDropdown.GetCurrentIndex() + 1}, true);
    }

    public void Set(int amount) {
        type.SetAmount(data, type.GetBaseValue() + type.GetStep() * amount);
    }
}
