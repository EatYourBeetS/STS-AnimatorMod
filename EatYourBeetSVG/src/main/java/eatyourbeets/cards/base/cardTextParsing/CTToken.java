package eatyourbeets.cards.base.cardTextParsing;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.helpers.FontHelper;
import eatyourbeets.utilities.ColoredString;

public abstract class CTToken
{
    protected static final GlyphLayout layout = new GlyphLayout();
    protected static final StringBuilder builder = new StringBuilder();
    protected static final StringBuilder tempBuilder = new StringBuilder();
    protected static final Color renderColor = Color.WHITE.cpy();

    public final CTTokenType type;
    public final String rawText;

    protected CTToken(CTTokenType type, String text)
    {
        this.type = type;
        this.rawText = text;
    }

    public float GetWidth(CTContext context)
    {
        return GetWidth(context.font, rawText);
    }

    public void Render(SpriteBatch sb, CTContext context)
    {
        Render(sb, context, rawText, context.color);
    }

    protected float GetWidth(BitmapFont font, String text)
    {
        layout.setText(font, text);
        return layout.width;
    }

    protected void Render(SpriteBatch sb, CTContext context, Color color)
    {
        Render(sb, context, rawText, color);
    }

    protected void Render(SpriteBatch sb, CTContext context, ColoredString string)
    {
        Render(sb, context, string.text, string.color != null ? string.color : context.color);
    }

    protected void Render(SpriteBatch sb, CTContext context, String text, Color color)
    {
        float width = GetWidth(context.font, text);

        renderColor.r = color.r;
        renderColor.g = color.g;
        renderColor.b = color.b;
        renderColor.a = color.a * context.card.transparency;
        FontHelper.renderRotatedText(sb, context.font, text, context.start_x + width / 2f, context.start_y, 0, 0, context.card.angle, true, renderColor);

        context.start_x += width;
    }
}