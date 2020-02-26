package eatyourbeets.ui.controls;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import eatyourbeets.ui.GUIElement;
import eatyourbeets.utilities.JavaUtilities;

public class GUI_Label extends GUIElement
{
    public Hitbox hb;
    public String text;

    private Color textColor;
    private BitmapFont font;
    private float fontScale;
    private float verticalRatio;
    private float horizontalRatio;
    private boolean smartText;

    public GUI_Label(BitmapFont font)
    {
        this(font, null);
    }

    public GUI_Label(BitmapFont font, Hitbox hb)
    {
        this.smartText = true;
        this.verticalRatio = 0.85f;
        this.horizontalRatio = 0.1f;
        this.textColor = Color.WHITE;
        this.fontScale = 1;
        this.font = font;
        this.text = "-";
        this.hb = hb;
    }

    public GUI_Label SetText(String text)
    {
        this.text = text;

        return this;
    }

    public GUI_Label SetText(String format, Object... args)
    {
        this.text = JavaUtilities.Format(format, args);

        return this;
    }

    public GUI_Label SetFont(BitmapFont font)
    {
        return SetFont(font, 1);
    }

    public GUI_Label SetFont(BitmapFont font, float fontScale)
    {
        this.font = font;
        this.fontScale = fontScale;

        return this;
    }

    public GUI_Label SetAlignment(float verticalRatio, float horizontalRatio)
    {
        this.verticalRatio = verticalRatio;
        this.horizontalRatio = horizontalRatio;
        this.smartText = false;

        return this;
    }

    public GUI_Label SetColor(Color textColor)
    {
        this.textColor = textColor;

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
        Render(sb, hb);
    }

    public void Render(SpriteBatch sb, Hitbox hb)
    {
        if (fontScale != 1)
        {
            font.getData().setScale(fontScale);
        }

        if (smartText)
        {
            final float step = hb.width * horizontalRatio;
            FontHelper.renderSmartText(sb, font, text, hb.x + step, hb.y + (hb.height * verticalRatio),
                    hb.width - (step * 2), font.getLineHeight(), textColor);
        }
        else if (horizontalRatio < 0.5f)
        {
            final float step = hb.width * horizontalRatio;
            FontHelper.renderFontLeft(sb, font, text, hb.x + step, hb.y + hb.height * verticalRatio, textColor);
        }
        else if (horizontalRatio > 0.5f)
        {
            final float step = hb.width * (1-horizontalRatio) * 2;
            FontHelper.renderFontRightAligned(sb, font, text, hb.x + hb.width - step, hb.y + hb.height * verticalRatio, textColor);
        }
        else
        {
            FontHelper.renderFontCentered(sb, font, text, hb.cX, hb.y + hb.height * verticalRatio, textColor);
        }

//        if (horizontallyCentered)
//        {
//            FontHelper.renderFontCentered(sb, font, text, hb.cX, hb.y + (hb.height * verticalRatio), textColor);
//        }
//        else
//        {
//            final float step = hb.width * horizontalRatio;
//            FontHelper.renderSmartText(sb, font, text, hb.x + step, hb.y - (hb.height * verticalRatio),
//                    hb.width - (step * 2), font.getLineHeight(), textColor);
//        }

        if (fontScale != 1)
        {
            font.getData().setScale(1f);
        }
    }
}
