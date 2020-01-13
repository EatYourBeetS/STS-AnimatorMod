package eatyourbeets.ui.controls;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import eatyourbeets.interfaces.UIControl;
import eatyourbeets.interfaces.csharp.ActionT0;
import eatyourbeets.utilities.RenderHelpers;

public class GenericButton implements UIControl
{
    protected TextureRenderer buttonRenderer;
    protected TextureRenderer buttonBorderRenderer;

    public final AdvancedHitbox hb;
    public float targetAlpha = 1f;
    public float currentAlpha = 1f;

    private Color textColor = Color.WHITE.cpy();
    private Color buttonColor = Color.WHITE.cpy();
    private boolean interactable;
    private ActionT0 onClick;
    private String text;

    public GenericButton(Texture buttonTexture, Color buttonColor, float x, float y)
    {
        this(buttonTexture, x, y);

        this.buttonColor = buttonColor;
    }

    public GenericButton(Texture buttonTexture, float x, float y)
    {
        this.hb = new AdvancedHitbox(x, y, UIControl.Scale(buttonTexture.getWidth()), UIControl.Scale(buttonTexture.getHeight()), false);
        this.buttonRenderer = RenderHelpers.ForTexture(buttonTexture);
        this.interactable = true;
        this.text = "-";
    }

    public GenericButton SetBorder(Texture borderTexture, Color color)
    {
        if (borderTexture == null)
        {
            this.buttonBorderRenderer = null;
        }
        else
        {
            this.buttonBorderRenderer = RenderHelpers.ForTexture(borderTexture, color);
        }

        return this;
    }

    public GenericButton SetInteractable(boolean interactable)
    {
        this.interactable = interactable;

        return this;
    }

    public GenericButton SetDimensions(float width, float height)
    {
        this.hb.resize(width, height);

        return this;
    }

    public GenericButton SetPosition(float x, float y)
    {
        this.hb.move(x, y);

        return this;
    }

    public GenericButton SetText(String text)
    {
        this.text = text;

        return this;
    }

    public GenericButton SetOnClick(ActionT0 onClick)
    {
        this.onClick = onClick;

        return this;
    }

    public GenericButton SetColor(Color buttonColor)
    {
        this.buttonColor = buttonColor;

        return this;
    }

    @Override
    public void Update()
    {
        this.currentAlpha = MathHelper.fadeLerpSnap(currentAlpha, targetAlpha);

        if (currentAlpha > 0)
        {
            this.hb.update();

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

            this.textColor.a = currentAlpha;
            this.buttonColor.a = currentAlpha;
        }
    }

    @Override
    public void Render(SpriteBatch sb)
    {
        if (currentAlpha > 0)
        {
            this.RenderButton(sb);

            BitmapFont font = FontHelper.buttonLabelFont;
            Color textColor = interactable ? this.textColor : TEXT_DISABLED_COLOR;
            if (FontHelper.getSmartWidth(font, text, 9999.0F, 0.0F) > (hb.width * 0.8))
            {
                RenderHelpers.WriteCentered(sb, font, text, hb, textColor, 0.8f);
            }
            else
            {
                RenderHelpers.WriteCentered(sb, font, text, hb, textColor);
            }

            this.hb.render(sb);
        }
    }

    protected void RenderButton(SpriteBatch sb)
    {
        buttonRenderer.SetColor(buttonColor).Draw(sb, hb);

        if (buttonBorderRenderer != null)
        {
            buttonBorderRenderer.Draw(sb, hb);
        }

        if (interactable && this.hb.hovered && !this.hb.clickStarted)
        {
            sb.setBlendFunction(770, 1);

            buttonRenderer.SetColor(HOVER_BLEND_COLOR).Draw(sb, hb);

            sb.setBlendFunction(770, 771);
        }
    }

    protected void OnHover()
    {
        CardCrawlGame.sound.play("UI_HOVER");
    }

    protected void OnClickStart()
    {
        this.hb.clickStarted = true;
        CardCrawlGame.sound.play("UI_CLICK_1");
    }

    protected void OnClick()
    {
        this.hb.clicked = false;

        if (interactable && onClick != null)
        {
            onClick.Invoke();
        }
    }
}
