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

    public final CTTokenType type;
    public final String text;

    protected CTToken(CTTokenType type, String text)
    {
        this.type = type;
        this.text = text;
    }

    public float GetWidth(CTContext context)
    {
        return GetWidth(context.font, text);
    }

    public void Render(SpriteBatch sb, CTContext context)
    {
        Render(sb, context, text, context.color);
    }

    protected float GetWidth(BitmapFont font, String text)
    {
        layout.setText(font, text);
        return layout.width;
    }

    protected void Render(SpriteBatch sb, CTContext context, Color color)
    {
        Render(sb, context, text, color);
    }

    protected void Render(SpriteBatch sb, CTContext context, ColoredString string)
    {
        Render(sb, context, string.text, string.color);
    }

    protected void Render(SpriteBatch sb, CTContext context, String text, Color color)
    {
        float width = GetWidth(context.font, text);

        FontHelper.renderRotatedText(sb, context.font, text, context.start_x + width / 2.0F, context.start_y, 0, 0, context.card.angle, true, color);

        context.start_x += width;
    }
}