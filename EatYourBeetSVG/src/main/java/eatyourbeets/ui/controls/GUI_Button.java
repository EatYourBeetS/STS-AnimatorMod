package eatyourbeets.ui.controls;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import eatyourbeets.effects.SFX;
import eatyourbeets.interfaces.delegates.ActionT0;
import eatyourbeets.resources.GR;
import eatyourbeets.ui.GUIElement;
import eatyourbeets.ui.hitboxes.AdvancedHitbox;
import eatyourbeets.utilities.Colors;
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
    public ActionT0 onClick;
    public String text;

    protected boolean darkenNonInteractableButton;
    protected BitmapFont font;
    protected float fontScale;
    protected float currentClickDelay = 0f;
    protected Color textColor = Color.WHITE.cpy();
    protected Color buttonColor;
    protected Color disabledButtonColor;

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

    public GUI_Button SetClickDelay(float delay)
    {
        this.clickDelay = delay;

        return this;
    }

    public GUI_Button SetOnClick(ActionT0 onClick)
    {
        this.onClick = onClick;

        return this;
    }

    public GUI_Button SetColor(Color buttonColor)
    {
        this.buttonColor = buttonColor.cpy();
        this.disabledButtonColor = Colors.Lerp(buttonColor, Color.BLACK, 0.4f);

        return this;
    }

    public GUI_Button SetTextColor(Color textColor)
    {
        this.textColor = textColor.cpy();

        return this;
    }

    public boolean IsInteractable()
    {
        return interactable && onClick != null;
    }

    @Override
    public void Update()
    {
        this.currentAlpha = MathHelper.fadeLerpSnap(currentAlpha, targetAlpha);

        if (currentClickDelay > 0)
        {
            this.currentClickDelay -= Gdx.graphics.getRawDeltaTime();
            return;
        }

        if (currentAlpha > 0)
        {
            this.hb.update();

            if (IsInteractable() && GR.UI.TryHover(hb))
            {
                if (this.hb.justHovered)
                {
                    OnHover();
                }

                if (this.hb.hovered && InputHelper.justClickedLeft)
                {
                    OnClickStart();
                }

                if (this.hb.clicked)
                {
                    OnClick();
                }
            }

            this.textColor.a = currentAlpha;
            this.buttonColor.a = currentAlpha;
        }
    }

    @Override
    public void Render(SpriteBatch sb)
    {
        if (currentAlpha > 0)
        {
            boolean interactable = IsInteractable();
            if (StringUtils.isNotEmpty(text))
            {
                this.RenderButton(sb, interactable, buttonColor);

                font.getData().setScale(fontScale);
                final Color color = interactable ? textColor : TEXT_DISABLED_COLOR;
                if (FontHelper.getSmartWidth(font, text, 9999f, 0f) > (hb.width * 0.7))
                {
                    RenderHelpers.WriteCentered(sb, font, text, hb, color, 0.8f);
                }
                else
                {
                    RenderHelpers.WriteCentered(sb, font, text, hb, color);
                }
                RenderHelpers.ResetFont(font);
            }
            else
            {
                this.RenderButton(sb, interactable, interactable ? buttonColor : disabledButtonColor);
            }

            this.hb.render(sb);
        }
    }

    protected void RenderButton(SpriteBatch sb, boolean interactable, Color color)
    {
        background.SetColor(color).Render(sb, hb);

        if (border != null)
        {
            border.Render(sb);
        }

        if (interactable && this.hb.hovered && !this.hb.clickStarted)
        {
            sb.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);

            background.SetColor(HOVER_BLEND_COLOR).Render(sb, hb);

            sb.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        }
    }

    protected void OnHover()
    {
        SFX.Play(SFX.UI_HOVER);
    }

    protected void OnClickStart()
    {
        this.hb.clickStarted = true;
        SFX.Play(SFX.UI_CLICK_1);
    }

    protected void OnClick()
    {
        this.hb.clicked = false;
        this.currentClickDelay = clickDelay;

        if (onClick != null)
        {
            this.onClick.Invoke();
        }
    }
}