package eatyourbeets.ui.controls;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import eatyourbeets.ui.GUIElement;
import eatyourbeets.utilities.JavaUtilities;
import eatyourbeets.utilities.RenderHelpers;

public class GUI_TextBox extends GUIElement
{
    public final Hitbox hb;

    private final GUI_Image texture;
    private Color backgroundColor;
    private Color textColor;
    private BitmapFont font;
    private String text;
    private float fontScale;
    private float verticalRatio;
    private boolean horizontallyCentered;

    public GUI_TextBox(Texture backgroundTexture, Hitbox hb)
    {
        this.texture = RenderHelpers.ForTexture(backgroundTexture).SetHitbox(hb);
        this.verticalRatio = 0.85f;
        this.text = "-";
        this.hb = hb;
    }

    public GUI_TextBox SetText(String text)
    {
        this.text = text;

        return this;
    }

    public GUI_TextBox SetText(String format, Object... args)
    {
        this.text = JavaUtilities.Format(format, args);

        return this;
    }

    public GUI_TextBox SetFont(BitmapFont font)
    {
        return SetFont(font, 1);
    }

    public GUI_TextBox SetFont(BitmapFont font, float fontScale)
    {
        this.font = font;
        this.fontScale = fontScale;

        return this;
    }

    public GUI_TextBox SetAlignment(float verticalRatio, boolean centerHorizontally)
    {
        this.verticalRatio = verticalRatio;
        this.horizontallyCentered = centerHorizontally;

        return this;
    }

    public GUI_TextBox SetColors(Color backgroundColor, Color textColor)
    {
        this.backgroundColor = backgroundColor;
        this.textColor = textColor;

        return this;
    }

    public GUI_TextBox SetFontColor(Color textColor)
    {
        this.textColor = textColor;

        return this;
    }

    @Override
    public void Update()
    {
        texture.hb.update();
    }

    @Override
    public void Render(SpriteBatch sb)
    {
        texture.SetColor(backgroundColor);
        texture.Render(sb, hb);

        if (fontScale != 1)
        {
            font.getData().setScale(fontScale);
        }

        if (horizontallyCentered)
        {
            FontHelper.renderFontCentered(sb, font, text, hb.cX, hb.y + (hb.height * verticalRatio), textColor);
        }
        else
        {
            final float step = hb.width * 0.1f;
            FontHelper.renderSmartText(sb, font, text, hb.x + step, hb.y + (hb.height * verticalRatio),
                    hb.width - (step * 2), font.getLineHeight(), textColor);
        }

        if (fontScale != 1)
        {
            font.getData().setScale(1f);
        }

        texture.hb.render(sb);
    }
}
