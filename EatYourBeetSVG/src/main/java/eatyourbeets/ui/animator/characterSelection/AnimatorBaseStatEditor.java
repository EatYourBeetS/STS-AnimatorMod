package eatyourbeets.ui.animator.characterSelection;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.screens.charSelect.CharacterOption;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.animator.misc.AnimatorLoadout;
import eatyourbeets.resources.animator.misc.AnimatorLoadoutData;
import eatyourbeets.ui.GUIElement;
import eatyourbeets.ui.controls.GUI_Button;
import eatyourbeets.ui.controls.GUI_Image;
import eatyourbeets.ui.controls.GUI_Label;
import eatyourbeets.ui.controls.GUI_TextBox;
import eatyourbeets.ui.hitboxes.AdvancedHitbox;
import eatyourbeets.ui.hitboxes.RelativeHitbox;

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
    protected Hitbox hb;
    protected GUI_Image image;
    protected GUI_Label label;
    protected GUI_Button decrease_button;
    protected GUI_Button increase_button;
    protected GUI_TextBox value_text;

    public AnimatorBaseStatEditor(AnimatorBaseStatEditor.Type type, float cX, float cY)
    {
        this.type = type;
        this.hb = new AdvancedHitbox(0, 0, ICON_SIZE * 2.5f, ICON_SIZE).SetPosition(cX, cY);

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

        value_text = new GUI_TextBox(GR.Common.Images.Panel_Rounded_Half_H.Texture(), new RelativeHitbox(hb, 0.5f, 0.75f, 0.5f, -0.1f))
        .SetBackgroundTexture(GR.Common.Images.Panel_Rounded_Half_H.Texture(), new Color(0.5f, 0.5f, 0.5f , 1f), 1.05f)
        .SetColors(new Color(0, 0, 0, 0.85f), Settings.CREAM_COLOR)
        .SetAlignment(0.5f, 0.5f)
        .SetFont(FontHelper.cardEnergyFont_L, 0.75f);
    }

    public AnimatorBaseStatEditor SetEstimatedValue(int value)
    {
        value_text.SetText(value).SetFontColor(value == 0 ? Settings.CREAM_COLOR : value < 0 ? Settings.RED_TEXT_COLOR : Settings.GREEN_TEXT_COLOR);

        return this;
    }
    
    @Override
    public void Update()
    {
        hb.update();
        image.Update();
        label.SetText(type.GetText(data)).Update();
        decrease_button.SetInteractable(CanDecrease()).Update();
        increase_button.SetInteractable(CanIncrease()).Update();
        value_text.Update();
    }

    @Override
    public void Render(SpriteBatch sb)
    {
        image.Render(sb);
        label.Render(sb);
        decrease_button.Render(sb);
        increase_button.Render(sb);
        value_text.Render(sb);
    }

    public void SetLoadout(AnimatorLoadoutData data)
    {
        this.data = data;
    }

    public boolean CanDecrease()
    {
        return type.GetAmount(data) > (type.GetBaseValue() - (type.GetStep() * AnimatorLoadout.MAX_STEP));
    }

    public void Decrease()
    {
        type.SetAmount(data, type.GetAmount(data) - type.GetStep());
    }

    public boolean CanIncrease()
    {
        return type.GetAmount(data) < (type.GetBaseValue() + (type.GetStep() * AnimatorLoadout.MAX_STEP));
    }

    public void Increase()
    {
        type.SetAmount(data, type.GetAmount(data) + type.GetStep());
    }
}
