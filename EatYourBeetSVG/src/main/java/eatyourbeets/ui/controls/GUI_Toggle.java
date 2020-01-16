package eatyourbeets.ui.controls;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import eatyourbeets.ui.GUIElement;
import eatyourbeets.interfaces.csharp.ActionT1;
import eatyourbeets.utilities.RenderHelpers;

public class GUI_Toggle extends GUIElement
{
    public Hitbox hb;
    public boolean toggled;

    private boolean interactable;
    private GUI_Image texture;
    private Color textureColor;
    private BitmapFont font;
    private String text;
    private ActionT1<Boolean> onToggle;

    public GUI_Toggle(Hitbox hb)
    {
        this.hb = hb;
        this.font = FontHelper.topPanelInfoFont;
        this.interactable = true;
        this.text = "-";
    }

    public GUI_Toggle SetInteractable(boolean interactable)
    {
        this.interactable = interactable;

        return this;
    }

    public GUI_Toggle SetFont(BitmapFont font)
    {
        this.font = font;

        return this;
    }

    public GUI_Toggle SetText(String text)
    {
        this.text = text;

        return this;
    }

    public GUI_Toggle SetTexture(Texture texture, Color color)
    {
        this.texture = RenderHelpers.ForTexture(texture).SetHitbox(hb);
        this.textureColor = color;

        return this;
    }

    public GUI_Toggle SetOnToggle(ActionT1<Boolean> onToggle)
    {
        this.onToggle = onToggle;

        return this;
    }

    public GUI_Toggle SetToggle(boolean value)
    {
        this.toggled = value;

        return this;
    }

    public void Toggle()
    {
        Toggle(!toggled);
    }

    public void Toggle(boolean value)
    {
        if (toggled != value)
        {
            toggled = value;

            if (onToggle != null)
            {
                onToggle.Invoke(value);
            }
        }
    }

    public boolean IsToggled()
    {
        return toggled;
    }

    @Override
    public void Update()
    {
        hb.update();

        if (hb.justHovered)
        {
            CardCrawlGame.sound.playA("UI_HOVER", -0.3F);
        }

        if (hb.hovered && InputHelper.justClickedLeft)
        {
            hb.clickStarted = true;
        }

        if (interactable && (hb.clicked || hb.hovered && CInputActionSet.select.isJustPressed()))
        {
            hb.clicked = false;
            CardCrawlGame.sound.playA("UI_CLICK_1", -0.2F);

            Toggle();
        }
    }

    @Override
    public void Render(SpriteBatch sb)
    {
        if (texture != null)
        {
            texture.SetColor(textureColor);
            texture.Render(sb);
        }

        Color fontColor;
        if (!interactable)
        {
            fontColor = TEXT_DISABLED_COLOR;
        }
        else if (hb.hovered)
        {
            fontColor = Settings.GOLD_COLOR;
        }
        else
        {
            fontColor = Settings.CREAM_COLOR;
        }

        FontHelper.renderFontCentered(sb, font, text, hb.cX + 18f * Settings.scale, hb.cY, fontColor);

        Texture img;
        if (toggled)
        {
            img = ImageMaster.COLOR_TAB_BOX_TICKED;
        }
        else
        {
            img = ImageMaster.COLOR_TAB_BOX_UNTICKED;
        }

        sb.setColor(fontColor);
        sb.draw(img, hb.x + 18f * Settings.scale, hb.cY - 24.0F, 24.0F, 24.0F, 48.0F, 48.0F, Settings.scale, Settings.scale,
                0.0F, 0, 0, 48, 48, false, false);

        hb.render(sb);
    }
}
