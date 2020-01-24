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

    public transient float width;
    public CTTokenType type;
    public String text;

    public void SetWidth(BitmapFont font)
    {
        layout.setText(font, text);
        width = layout.width;
    }

    @Override
    public String toString()
    {
        return text;
    }

    public void Render(SpriteBatch sb, CTContext context)
    {
        final AbstractCard card = context.card;

        layout.setText(context.font, text);

        FontHelper.renderRotatedText(sb, context.font, text, card.current_x, card.current_y, context.start_x - card.current_x + layout.width / 2.0F,
        (float)context.line * 1.45F * -context.height + context.start_y - card.current_y + -6.0F, card.angle, true, context.color);

        context.start_x += layout.width; // context.start_x = (float) Math.round(context.start_x + layout.width);
    }
}