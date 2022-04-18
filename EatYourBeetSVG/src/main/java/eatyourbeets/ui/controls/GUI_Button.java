package eatyourbeets.ui.controls;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import eatyourbeets.cards.base.EYBCardTooltip;
import eatyourbeets.effects.SFX;
import eatyourbeets.interfaces.delegates.ActionT0;
import eatyourbeets.interfaces.delegates.ActionT1;
import eatyourbeets.interfaces.delegates.ActionT2;
import eatyourbeets.resources.GR;
import eatyourbeets.ui.GUIElement;
import eatyourbeets.ui.hitboxes.AdvancedHitbox;
import eatyourbeets.utilities.Colors;
import eatyourbeets.utilities.GenericCallback;
import eatyourbeets.utilities.InputManager;
import eatyourbeets.utilities.RenderHelpers;
import org.apache.commons.lang3.StringUtils;

public class GUI_Button extends GUIElement
{
    public final Hitbox hb;
    public GUI_Image background;
    public GUI_Image border;

    public float clickDelay = 0f;
    public float targetAlpha = 1f;
    public float currentAlpha = 1f;
    public boolean interactable;
    public boolean forceRenderTooltip;
    public EYBCardTooltip tooltip;
    public GenericCallback<GUI_Button> onLeftClick;
    public GenericCallback<GUI_Button> onRightClick;
    public String text;

    public Color textColor = Color.WHITE.cpy();
    public Color buttonColor;
    public Color hoverBlendColor = HOVER_BLEND_COLOR.cpy();
    public Color disabledButtonColor;
    public Color disabledTextColor = TEXT_DISABLED_COLOR.cpy();

    protected boolean smartText;
    protected BitmapFont font;
    protected float fontScale;
    protected float currentClickDelay = 0f;

    public GUI_Button(Texture buttonTexture, float x, float y)
    {
        this(buttonTexture, new AdvancedHitbox(x, y, Scale(buttonTexture.getWidth()), Scale(buttonTexture.getHeight())));
    }

    public GUI_Button(Texture buttonTexture, Hitbox hitbox)
    {
        this.hb = hitbox;
        this.background = RenderHelpers.ForTexture(buttonTexture);
        this.interactable = true;
        this.text = "-";
        this.font = FontHelper.buttonLabelFont;
        this.fontScale = 1f;
        SetColor(Color.WHITE);
    }

    public GUI_Button SetBorder(Texture borderTexture, Color color)
    {
        if (borderTexture == null)
        {
            this.border = null;
        }
        else
        {
            this.border = RenderHelpers.ForTexture(borderTexture, color).SetHitbox(hb);
        }

        return this;
    }

    public GUI_Button SetFont(BitmapFont font, float fontScale)
    {
        if (font != null)
        {
            this.font = font;
        }

        this.fontScale = fontScale;

        return this;
    }

    public GUI_Button SetInteractable(boolean interactable)
    {
        this.interactable = interactable;

        return this;
    }

    public GUI_Button SetDimensions(float width, float height)
    {
        this.hb.resize(width, height);

        return this;
    }

    public GUI_Button SetPosition(float cX, float cY)
    {
        this.hb.move(cX, cY);

        return this;
    }

    public GUI_Button SetText(String text)
    {
        this.text = text;

        return this;
    }

    public GUI_Button SetText(String text, boolean smartText)
    {
        this.text = text;
        this.smartText = smartText;

        return this;
    }

    public GUI_Button SetClickDelay(float delay)
    {
        this.clickDelay = delay;

        return this;
    }

    public GUI_Button SetOnClick(ActionT0 onClick)
    {
        this.onLeftClick = GenericCallback.FromT0(onClick);

        return this;
    }

    public GUI_Button SetOnClick(ActionT1<GUI_Button> onClick)
    {
        this.onLeftClick = GenericCallback.FromT1(onClick);

        return this;
    }

    public <T> GUI_Button SetOnClick(T state, ActionT2<T, GUI_Button> onClick)
    {
        this.onLeftClick = GenericCallback.FromT2(onClick, state);

        return this;
    }

    public GUI_Button SetOnRightClick(ActionT0 onClick)
    {
        this.onRightClick = GenericCallback.FromT0(onClick);

        return this;
    }

    public GUI_Button SetOnRightClick(ActionT1<GUI_Button> onClick)
    {
        this.onRightClick = GenericCallback.FromT1(onClick);

        return this;
    }

    public <T> GUI_Button SetOnRightClick(T state, ActionT2<T, GUI_Button> onClick)
    {
        this.onRightClick = GenericCallback.FromT2(onClick, state);

        return this;
    }

    public GUI_Button SetNonInteractableButtonColor(Color color)
    {
        this.disabledButtonColor = color == null ? buttonColor.cpy() : color.cpy();

        return this;
    }

    public GUI_Button SetNonInteractableTextColor(Color color)
    {
        this.disabledTextColor = color == null ? textColor.cpy() : color.cpy();

        return this;
    }

    public GUI_Button SetColor(Color buttonColor)
    {
        this.buttonColor = buttonColor.cpy();
        this.disabledButtonColor = Colors.Lerp(buttonColor, Color.BLACK, 0.4f);

        return this;
    }

    public GUI_Button SetColor(Color buttonColor, Color hoverBlendColor)
    {
        this.buttonColor = buttonColor.cpy();
        this.disabledButtonColor = Colors.Lerp(buttonColor, Color.BLACK, 0.4f);
        this.hoverBlendColor = hoverBlendColor.cpy();

        return this;
    }

    public GUI_Button SetTextColor(Color textColor)
    {
        this.textColor = textColor.cpy();

        return this;
    }

    public GUI_Button SetTooltip(String title, String description, boolean forceRender)
    {
        return SetTooltip(new EYBCardTooltip(title, description), forceRender);
    }

    public GUI_Button SetTooltip(EYBCardTooltip tooltip, boolean forceRender)
    {
        this.tooltip = tooltip;
        this.forceRenderTooltip = forceRender;

        return this;
    }

    public GUI_Button ShowTooltip(boolean show)
    {
        if (tooltip != null)
        {
            this.tooltip.canRender = show;
        }

        return this;
    }

    public boolean IsInteractable()
    {
        return interactable && onLeftClick != null;
    }

    @Override
    public void Update()
    {
        if (currentClickDelay > 0)
        {
            this.currentClickDelay -= GR.UI.Delta();
        }

        this.currentAlpha = MathHelper.fadeLerpSnap(currentAlpha, targetAlpha);
        if ((currentAlpha <= 0))
        {
            return;
        }

        this.hb.update();

        if (IsInteractable() && GR.UI.TryHover(hb))
        {
            if (this.hb.justHovered)
            {
                OnJustHovered();
            }

            if (this.hb.hovered)
            {
                if (currentClickDelay <= 0)
                {
                    if (InputManager.RightClick.IsJustPressed())
                    {
                        OnRightClick();
                    }
                    else if (InputHelper.justClickedLeft)
                    {
                        OnClickStart();
                    }
                }
            }

            if (this.hb.clicked)
            {
                OnLeftClick();
            }
        }

        if (this.hb.hovered && tooltip != null && tooltip.canRender)
        {
            EYBCardTooltip.QueueTooltip(tooltip, hb, forceRenderTooltip);
        }
    }

    @Override
    public void Render(SpriteBatch sb)
    {
        this.buttonColor.a = this.textColor.a = currentAlpha;
        if (currentAlpha <= 0)
        {
            return;
        }

        final boolean interactable = IsInteractable();
        if (StringUtils.isNotEmpty(text))
        {
            this.RenderButton(sb, interactable, buttonColor);

            final Color color = interactable ? textColor : disabledTextColor;
            final float scale = (FontHelper.getSmartWidth(font, text, 9999f, 0f) > (hb.width * 0.7)) ? 0.8f : 1f;

            if (smartText)
            {
                font.getData().setScale(fontScale * scale);
                final float width = RenderHelpers.GetSmartWidth(font, text);
                RenderHelpers.WriteSmartText(sb, font, text, hb.cX - (width * 0.5f), hb.y + (hb.height * 0.625f), hb.width, color);
            }
            else
            {
                font.getData().setScale(fontScale);
                RenderHelpers.WriteCentered(sb, font, text, hb, color, scale);
            }

            RenderHelpers.ResetFont(font);
        }
        else
        {
            this.RenderButton(sb, interactable, interactable ? buttonColor : disabledButtonColor);
        }

        this.hb.render(sb);
    }

    protected void RenderButton(SpriteBatch sb, boolean interactable, Color color)
    {
        background.SetColor(color).Render(sb, hb);

        if (border != null && border.isActive)
        {
            border.color.a = currentAlpha;
            border.Render(sb, hb);
        }

        if (interactable && this.hb.hovered && !this.hb.clickStarted)
        {
            sb.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);

            background.SetColor(hoverBlendColor).Render(sb, hb);

            sb.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        }
    }

    protected void OnJustHovered()
    {
        SFX.Play(SFX.UI_HOVER);
    }

    protected void OnClickStart()
    {
        this.hb.clickStarted = true;
        SFX.Play(SFX.UI_CLICK_1);
    }

    protected void OnLeftClick()
    {
        this.hb.clicked = false;
        this.currentClickDelay = clickDelay;

        if (onLeftClick != null)
        {
            this.onLeftClick.Complete(this);
        }
    }

    protected void OnRightClick()
    {
        this.hb.clicked = false;
        this.currentClickDelay = clickDelay;

        if (onRightClick != null)
        {
            this.onRightClick.Complete(this);
        }
    }
}