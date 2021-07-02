package eatyourbeets.ui.controls;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import eatyourbeets.interfaces.delegates.ActionT0;
import eatyourbeets.ui.AdvancedHitbox;
import eatyourbeets.ui.GUIElement;
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
    public String text;

    protected float currentClickDelay = 0f;
    protected Color textColor = Color.WHITE.cpy();
    protected Color buttonColor = Color.WHITE.cpy();
    protected ActionT0 onClick;

    public GUI_Button(Texture buttonTexture, float x, float y)
    {
        this(buttonTexture, new AdvancedHitbox(x, y, Scale(buttonTexture.getWidth()), Scale(buttonTexture.getHeight()), false));
    }

    public GUI_Button(Texture buttonTexture, Hitbox hitbox)
    {
        this.hb = hitbox;
        this.background = RenderHelpers.ForTexture(buttonTexture);
        this.interactable = true;
        this.text = "-";
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

    public GUI_Button SetPosition(float x, float y)
    {
        this.hb.move(x, y);

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

        return this;
    }

    public GUI_Button SetTextColor(Color textColor)
    {
        this.textColor = textColor.cpy();

        return this;
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

            if (StringUtils.isNotEmpty(text))
            {
                BitmapFont font = FontHelper.buttonLabelFont;
                Color textColor = interactable ? this.textColor : TEXT_DISABLED_COLOR;
                if (FontHelper.getSmartWidth(font, text, 9999f, 0f) > (hb.width * 0.7))
                {
                    RenderHelpers.WriteCentered(sb, font, text, hb, textColor, 0.8f);
                }
                else
                {
                    RenderHelpers.WriteCentered(sb, font, text, hb, textColor);
                }
            }

            this.hb.render(sb);
        }
    }

    protected void RenderButton(SpriteBatch sb)
    {
        background.SetColor(buttonColor).Render(sb, hb);

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
        this.currentClickDelay = clickDelay;

        if (interactable && onClick != null)
        {
            onClick.Invoke();
        }
    }
}
