package eatyourbeets.cards.base.cardTextParsing;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.utilities.JUtils;

import java.util.ArrayList;

public class CTLine
{
    protected final static float IMG_HEIGHT = 420f * Settings.scale;
    protected final static float IMG_WIDTH = 300f * Settings.scale;
    protected final static float DESC_BOX_WIDTH = IMG_WIDTH * 0.81f;//0.79f;
    protected final static float DESC_OFFSET_Y = IMG_HEIGHT * 0.255f;
    protected final ArrayList<CTToken> tokens = new ArrayList<>();
    protected final CTContext context;
    protected int tokenIndex;

    public float width = 0;

    public CTLine(CTContext context)
    {
        this.context = context;
    }

    public void Add(CTToken token)
    {
        float tokenWidth = token.GetWidth(context);
        if (tokens.isEmpty())
        {
            if (token.type != CTTokenType.Whitespace)
            {
                tokens.add(token);
                width += tokenWidth;
            }
        }
        else if ((tokenWidth + width) < DESC_BOX_WIDTH || (token.type == CTTokenType.Punctuation && token.rawText.length() == 1))
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

    public CTToken Get(CTContext context, int index)
    {
        try {
            return (index >= tokens.size()) ? null : tokens.get(index);
        }
        catch (Exception e)
        {
            JUtils.LogError(this, "ERROR: Cannot parse token at index "+index+" for card "+context.card.name);
            throw e;
        }
    }

    public float CalculateHeight(BitmapFont font)
    {
        if (tokens.isEmpty())
        {
            return font.getCapHeight() * 0.5f;
        }
        else
        {
            return font.getCapHeight();
        }
    }

    public void Render(SpriteBatch sb)
    {
        final EYBCard card = context.card;

        context.start_x = card.current_x - (width * card.drawScale * 0.5f);
        context.start_y = context.start_y - (CalculateHeight(context.font) * 1.45f);

        for (tokenIndex = 0; tokenIndex < tokens.size(); tokenIndex++)
        {
            tokens.get(tokenIndex).Render(sb, context);
        }
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        for (CTToken token : tokens)
        {
            sb.append(token.rawText);
        }

        return sb.toString();
    }

    protected void TrimEnd()
    {
        int size = tokens.size();
        if (size > 0 && tokens.get(size - 1).type == CTTokenType.Whitespace)
        {
            width -= tokens.remove(size - 1).GetWidth(context);
            TrimEnd();
        }
    }
}