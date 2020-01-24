package eatyourbeets.misc.cardTextParsing;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import eatyourbeets.cards.base.EYBCard;

import java.util.ArrayList;

public class CTLine
{
    protected final static float IMG_WIDTH = 300.0F * Settings.scale;
    protected final static float DESC_BOX_WIDTH = Settings.BIG_TEXT_MODE ? IMG_WIDTH * 0.95F : IMG_WIDTH * 0.79F;
    protected final ArrayList<CTToken> tokens = new ArrayList<>();

    public float width = 0;

    public int Count()
    {
        return tokens.size();
    }

    public CTToken Get(int index)
    {
        return tokens.get(index);
    }

    public void Add(CTToken token)
    {
        tokens.add(token);
        width += token.width;
    }

    public void Remove(int index)
    {
        width -= tokens.remove(index).width;
    }

    public void Render(SpriteBatch sb, CTContext context)
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

        for (CTToken token : tokens)
        {
            token.Render(sb, context);
        }

        context.line += 1;
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

    public void TrimEnd()
    {
        int size = tokens.size();
        if (size > 0 && Get(size - 1).type == CTTokenType.Whitespace)
        {
            Remove(size - 1);
        }
    }

    public void TrimStart()
    {
        if (tokens.size() > 0 && Get(0).type == CTTokenType.Whitespace)
        {
            Remove(0);
        }
    }
}