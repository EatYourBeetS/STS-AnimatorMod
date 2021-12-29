package pinacolada.ui.characterSelection;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.screens.charSelect.CharacterOption;
import eatyourbeets.utilities.EYBFontHelper;
import pinacolada.resources.GR;
import pinacolada.resources.pcl.misc.PCLLoadoutData;
import pinacolada.ui.GUIElement;
import pinacolada.ui.controls.GUI_Button;
import pinacolada.ui.controls.GUI_Dropdown;
import pinacolada.ui.controls.GUI_Image;
import pinacolada.ui.controls.GUI_Label;
import pinacolada.ui.hitboxes.AdvancedHitbox;
import pinacolada.ui.hitboxes.RelativeHitbox;
import pinacolada.utilities.PCLJUtils;

public class PCLBaseStatEditor extends GUIElement
{

    // TODO allow for fractional values, ADD potion slot editor
    public enum StatType
    {
        Gold(ImageMaster.TP_GOLD, Settings.GOLD_COLOR, 99, 15, -6, 6),
        HP(ImageMaster.TP_HP, Settings.RED_TEXT_COLOR, 70, 2, -6, 6),
        PotionSlot(ImageMaster.POTION_PLACEHOLDER, Settings.CREAM_COLOR, 0, 1, -1, 0),
        CommonUpgrade(ImageMaster.TP_ASCENSION, Settings.CREAM_COLOR, 0, 1, 0, 2);

        public void SetAmount(PCLLoadoutData data, int amount)
        {
            if (data != null) {
                data.Values.put(this, amount);
            }
        }

        public int GetAmount(PCLLoadoutData data)
        {
            return BaseAmount + AmountStep * (data != null ? data.Values.getOrDefault(this, 0) : 0);
        }

        public String GetText(PCLLoadoutData data)
        {
            if (data == null) {
                return "";
            }
            switch (this) {
                case Gold:
                    return CharacterOption.TEXT[5] + GetAmount(data);
                case HP:
                    return CharacterOption.TEXT[4] + GetAmount(data);
                case PotionSlot:
                    return GR.PCL.Strings.Rewards.PotionSlot + GetAmount(data);
                case CommonUpgrade:
                    return GR.PCL.Strings.Rewards.CommonUpgrade + GetAmount(data);
                default:
                    return "";
            }
        }

        public final Texture Icon;
        public final Color LabelColor;
        public final int BaseAmount;
        public final int AmountStep;
        public final int MinValue;
        public final int MaxValue;

        StatType(Texture icon, Color labelColor, int baseAmount, int amountStep, int minValue, int maxValue) {
            this.Icon = icon;
            this.LabelColor = labelColor;
            this.BaseAmount = baseAmount;
            this.AmountStep = amountStep;
            this.MinValue = minValue;
            this.MaxValue = maxValue;
        }
    }

    public StatType type;
    public PCLLoadoutData data;

    protected static final float ICON_SIZE = 64f * Settings.scale;
    protected boolean interactable;
    protected Hitbox hb;
    protected GUI_Image image;
    protected GUI_Label label;
    protected GUI_Button decrease_button;
    protected GUI_Button increase_button;
    protected GUI_Dropdown<Integer> valueDropdown;
    protected PCLLoadoutEditor editor;

    public PCLBaseStatEditor(StatType type, float cX, float cY, PCLLoadoutEditor editor)
    {
        this.type = type;
        this.hb = new AdvancedHitbox(0, 0, ICON_SIZE * 2.5f, ICON_SIZE).SetPosition(cX, cY);
        this.editor = editor;

        final float w = hb.width;
        final float h = hb.height;

        image = new GUI_Image(type.Icon, new RelativeHitbox(hb, ICON_SIZE, ICON_SIZE, (ICON_SIZE * 0.5f), h * 0.5f, false));
        label = new GUI_Label(FontHelper.tipHeaderFont, new RelativeHitbox(hb, w - ICON_SIZE, h, (w + ICON_SIZE) * 0.5f, h * 0.5f, false))
        .SetAlignment(0.5f, 0f, false)
        .SetColor(type.LabelColor);

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
                .SetItems(PCLJUtils.RangeArray(type.MinValue, type.MaxValue));
    }

    public PCLBaseStatEditor SetEstimatedValue(int value)
    {
        valueDropdown.SetSelection(value, true);

        return this;
    }

    public PCLBaseStatEditor SetInteractable(boolean interactable)
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

    public void SetLoadout(PCLLoadoutData data)
    {
        this.data = data;
        valueDropdown.SetSelection(data.Values.getOrDefault(type, 0), true);
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
        type.SetAmount(data, amount);
    }
}
