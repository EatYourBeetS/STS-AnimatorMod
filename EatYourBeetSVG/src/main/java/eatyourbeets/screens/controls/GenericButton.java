package eatyourbeets.screens.controls;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import eatyourbeets.interfaces.UIControl;
import eatyourbeets.interfaces.csharp.ActionT0;
import eatyourbeets.utilities.RenderHelpers;

public class GenericButton implements UIControl
{
    private static final Color HOVER_BLEND_COLOR = new Color(1.0F, 1.0F, 1.0F, 0.3F);
    private static final Color TEXT_DISABLED_COLOR = new Color(0.6F, 0.6F, 0.6F, 1.0F);

    protected RenderHelpers.TextureRenderer buttonRenderer;
    protected RenderHelpers.TextureRenderer buttonBorderRenderer;

    public final DraggableHitbox hb;
    public float targetAlpha = 1f;
    public float currentAlpha = 1f;
    public Color textColor = Color.WHITE.cpy();
    public Color buttonColor = Color.WHITE.cpy();
    public boolean isDisabled;
    public ActionT0 onClick;
    public String text;

    public GenericButton(Texture buttonTexture, Color buttonColor, float x, float y)
    {
        this(buttonTexture, x, y);

        this.buttonColor = buttonColor;
    }

    public GenericButton(Texture buttonTexture, float x, float y)
    {
        this.hb = new DraggableHitbox(x, y, Scale(buttonTexture.getWidth()), Scale(buttonTexture.getHeight()), true);
        this.buttonRenderer = RenderHelpers.ForTexture(buttonTexture);
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
            Color textColor = isDisabled ? TEXT_DISABLED_COLOR : this.textColor;
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

        if (this.hb.hovered && !this.hb.clickStarted)
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

        if (!isDisabled && onClick != null)
        {
            onClick.Invoke();
        }
    }

    protected float Scale(float value)
    {
        return Settings.scale * value;
    }
}
