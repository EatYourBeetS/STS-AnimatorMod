package eatyourbeets.misc.cardTextParsing;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import eatyourbeets.cards.base.EYBCard;

import java.util.ArrayList;

public class CTLine
{
    protected final static float IMG_HEIGHT = 420.0F * Settings.scale;
    protected final static float IMG_WIDTH = 300.0F * Settings.scale;
    protected final static float DESC_BOX_WIDTH = Settings.BIG_TEXT_MODE ? IMG_WIDTH * 0.95F : IMG_WIDTH * 0.79F;
    protected final static float DESC_OFFSET_Y = Settings.BIG_TEXT_MODE ? IMG_HEIGHT * 0.24F : IMG_HEIGHT * 0.255F;
    protected final ArrayList<CTToken> tokens = new ArrayList<>();
    protected final CTContext context;

    public float width = 0;

    public CTLine(CTContext context)
    {
        this.context = context;
    }

    public void Add(CTToken token)
    {
        float tokenWidth = token.GetWidth(context.font);
        if (tokens.isEmpty())
        {
            if (token.type != CTTokenType.Whitespace)
            {
                tokens.add(token);
                width += tokenWidth;
            }
        }
        else if ((tokenWidth + width) < DESC_BOX_WIDTH || (token.type == CTTokenType.Punctuation && token.text.length() == 1))
        {
            tokens.add(token);
            width += tokenWidth;
        }
        else
        {
            CTLine newLine = context.AddLine();

            if (token.type != CTTokenType.Whitespace)
            {
                newLine.tokens.add(token);
                newLine.width += tokenWidth;
            }

            TrimEnd();
        }
    }

    public void Render(SpriteBatch sb)
    {
        final EYBCard card = context.card;

        if (!Settings.lineBreakViaCharacter)
        {
            context.start_x = card.current_x - (width * card.drawScale * 0.5f);
        }
        else if (Settings.leftAlignCards)
        {
            context.start_x = card.current_x - (DESC_BOX_WIDTH * card.drawScale * 0.5f) + 2.0F * Settings.scale;
        }
        else
        {
            context.start_x = card.current_x - (width * card.drawScale * 0.5f) - 14.0F * Settings.scale;
        }

        context.start_y = (card.current_y - IMG_HEIGHT * card.drawScale * 0.5f + DESC_OFFSET_Y * card.drawScale)
                        + (context.lines.size() * context.lineHeight * 0.775F - context.lineHeight * 0.375F)
                        - (context.lineHeight * context.lineIndex * 1.45F) - 6f;

        for (CTToken token : tokens)
        {
            token.Render(sb, context);
        }

        context.lineIndex += 1;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        for (CTToken token : tokens)
        {
            sb.append(token.text);
        }

        return sb.toString();
    }

    protected void TrimEnd()
    {
        int size = tokens.size();
        if (size > 0 && tokens.get(size - 1).type == CTTokenType.Whitespace)
        {
            width -= tokens.remove(size - 1).GetWidth(context.font);
        }
    }
}