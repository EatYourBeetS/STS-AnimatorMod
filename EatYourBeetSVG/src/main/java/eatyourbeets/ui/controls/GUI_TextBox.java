package eatyourbeets.ui.controls;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import eatyourbeets.ui.GUIElement;

public class GUI_TextBox extends GUIElement
{
    public final Hitbox hb;
    public final GUI_Image image;
    public final GUI_Label label;

    public GUI_TextBox(Texture backgroundTexture, Hitbox hb)
    {
        this.label = new GUI_Label(FontHelper.buttonLabelFont);
        this.image = new GUI_Image(backgroundTexture);
        this.hb = hb;
    }

    public GUI_TextBox SetText(String text)
    {
        this.label.SetText(text);

        return this;
    }

    public GUI_TextBox SetText(String format, Object... args)
    {
        this.label.SetText(format, args);

        return this;
    }

    public GUI_TextBox SetFont(BitmapFont font, float fontScale)
    {
        this.label.SetFont(font, 1);

        return this;
    }

    public GUI_TextBox SetAlignment(float verticalRatio, float horizontalRatio)
    {
        return SetAlignment(verticalRatio, horizontalRatio, false);
    }

    public GUI_TextBox SetAlignment(float verticalRatio, float horizontalRatio, boolean smartText)
    {
        this.label.SetAlignment(verticalRatio, horizontalRatio, smartText);

        return this;
    }

    public GUI_TextBox SetColors(Color backgroundColor, Color textColor)
    {
        this.image.SetColor(backgroundColor);
        this.label.SetColor(textColor);

        return this;
    }

    public GUI_TextBox SetPosition(float x, float y)
    {
        this.hb.move(x, y);

        return this;
    }

    public GUI_TextBox SetFontColor(Color textColor)
    {
        this.label.SetColor(textColor);

        return this;
    }

    @Override
    public void Update()
    {
        hb.update();
    }

    @Override
    public void Render(SpriteBatch sb)
    {
        image.Render(sb, hb);
        label.Render(sb, hb);

        hb.render(sb);
    }
}
