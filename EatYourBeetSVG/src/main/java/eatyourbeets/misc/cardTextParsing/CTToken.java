package eatyourbeets.misc.cardTextParsing;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.helpers.FontHelper;

public abstract class CTToken
{
    protected static final GlyphLayout layout = new GlyphLayout();
    protected static final StringBuilder builder = new StringBuilder();
    protected static final StringBuilder tempBuilder = new StringBuilder();

    public CTTokenType type;
    public String text;

    protected CTToken(CTTokenType type, String text)
    {
        this.type = type;
        this.text = text;
    }

    public float GetWidth(BitmapFont font)
    {
        layout.setText(font, text);
        return layout.width;
    }

    @Override
    public String toString()
    {
        return text;
    }

    public void Render(SpriteBatch sb, CTContext context)
    {
        final AbstractCard card = context.card;
        float width = GetWidth(context.font);

        FontHelper.renderRotatedText(sb, context.font, text, context.start_x + width / 2.0F, context.start_y, 0, 0, card.angle, true, context.color);

        context.start_x += width; // context.start_x = (float) Math.round(context.start_x + layout.width);
    }
}