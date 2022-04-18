package eatyourbeets.ui.animator.characterSelection;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.screens.charSelect.CharacterOption;
import eatyourbeets.cards.base.EYBCardTooltip;
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

public abstract class AnimatorBaseStatEditor extends GUIElement
{
    public AnimatorLoadoutData data;

    protected static final float ICON_SIZE = 64f * Settings.scale;
    protected boolean interactable;
    protected Hitbox hb;
    protected GUI_Image image;
    protected GUI_Label label;
    protected GUI_Button decrease_button;
    protected GUI_Button increase_button;
    protected GUI_TextBox value_text;

    protected AnimatorBaseStatEditor(float cX, float cY)
    {
        this.hb = new AdvancedHitbox(0, 0, ICON_SIZE * 2.5f, ICON_SIZE).SetPosition(cX, cY);

        final float w = hb.width;
        final float h = hb.height;

        image = new GUI_Image(GetIcon(), new RelativeHitbox(hb, ICON_SIZE, ICON_SIZE, (ICON_SIZE * 0.5f), h * 0.5f, false));
        label = new GUI_Label(FontHelper.tipHeaderFont, new RelativeHitbox(hb, w - ICON_SIZE, h, (w + ICON_SIZE) * 0.5f, h * 0.5f, false))
        .SetAlignment(0.5f, 0f, false)
        .SetColor(GetColor());

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
        label.SetText(GetText(data)).Update();
        decrease_button.SetInteractable(interactable && CanDecrease()).Update();
        increase_button.SetInteractable(interactable && CanIncrease()).Update();
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
        return GetAmount(data) > (GetBaseValue() - (GetStep() * GetMaxStep()));
    }

    public void Decrease()
    {
        SetAmount(data, GetAmount(data) - GetStep());
    }

    public boolean CanIncrease()
    {
        return GetAmount(data) < (GetBaseValue() + (GetStep() * GetMaxStep()));
    }

    public void Increase()
    {
        SetAmount(data, GetAmount(data) + GetStep());
    }

    public abstract Texture GetIcon();
    public abstract Color GetColor();
    public abstract void SetAmount(AnimatorLoadoutData data, int amount);
    public abstract int GetBaseValue();
    public abstract int GetStep();
    public abstract int GetMaxStep();
    public abstract int GetAmount(AnimatorLoadoutData data);
    public abstract String GetText(AnimatorLoadoutData data);

    public static class HP extends AnimatorBaseStatEditor
    {
        protected HP(float cX, float cY)
        {
            super(cX, cY);
        }

        @Override
        public Texture GetIcon()
        {
            return ImageMaster.TP_HP;
        }

        @Override
        public Color GetColor()
        {
            return Settings.RED_TEXT_COLOR;
        }

        @Override
        public void SetAmount(AnimatorLoadoutData data, int amount)
        {
            data.HP = amount;
        }

        @Override
        public int GetBaseValue()
        {
            return AnimatorLoadout.BASE_HP;
        }

        @Override
        public int GetStep()
        {
            return AnimatorLoadout.HP_STEP;
        }

        @Override
        public int GetMaxStep()
        {
            return AnimatorLoadout.MAX_STEP_HP;
        }

        @Override
        public int GetAmount(AnimatorLoadoutData data)
        {
            return data.HP;
        }

        @Override
        public String GetText(AnimatorLoadoutData data)
        {
            return CharacterOption.TEXT[4]  + " " + GetAmount(data);
        }
    }

    public static class Gold extends AnimatorBaseStatEditor
    {
        protected Gold(float cX, float cY)
        {
            super(cX, cY);
        }

        @Override
        public Texture GetIcon()
        {
            return ImageMaster.TP_GOLD;
        }

        @Override
        public Color GetColor()
        {
            return Settings.GOLD_COLOR;
        }

        @Override
        public void SetAmount(AnimatorLoadoutData data, int amount)
        {
            data.Gold = amount;
        }

        @Override
        public int GetBaseValue()
        {
            return AnimatorLoadout.BASE_GOLD;
        }

        @Override
        public int GetStep()
        {
            return AnimatorLoadout.GOLD_STEP;
        }

        @Override
        public int GetMaxStep()
        {
            return AnimatorLoadout.MAX_STEP_GOLD;
        }

        @Override
        public int GetAmount(AnimatorLoadoutData data)
        {
            return data.Gold;
        }

        @Override
        public String GetText(AnimatorLoadoutData data)
        {
            return CharacterOption.TEXT[5] + " " + GetAmount(data);
        }
    }

    public static class Buff extends AnimatorBaseStatEditor
    {
        protected static final EYBCardTooltip tooltip = new EYBCardTooltip("Buff", "Start your first #b3 #yNormal combats with #b+1 [Strength][Dexterity][Focus].");

        protected Buff(float cX, float cY)
        {
            super(cX, cY);

            increase_button.SetTooltip(tooltip, true);
        }

        @Override
        public boolean CanDecrease()
        {
            return GetAmount(data) > 0;
        }

        @Override
        public Texture GetIcon()
        {
            return ImageMaster.INTENT_MAGIC;
        }

        @Override
        public Color GetColor()
        {
            return Settings.BLUE_TEXT_COLOR;
        }

        @Override
        public void SetAmount(AnimatorLoadoutData data, int amount)
        {
            data.Buff = amount;
        }

        @Override
        public int GetBaseValue()
        {
            return AnimatorLoadout.BASE_BUFF;
        }

        @Override
        public int GetStep()
        {
            return AnimatorLoadout.BUFF_STEP;
        }

        @Override
        public int GetMaxStep()
        {
            return AnimatorLoadout.MAX_STEP_BUFF;
        }

        @Override
        public int GetAmount(AnimatorLoadoutData data)
        {
            return data.Buff;
        }

        @Override
        public String GetText(AnimatorLoadoutData data)
        {
            return "Buff: " + GetAmount(data);
        }
    }
}
