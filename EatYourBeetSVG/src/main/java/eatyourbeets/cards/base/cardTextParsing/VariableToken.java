package eatyourbeets.cards.base.cardTextParsing;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import eatyourbeets.cards.base.AffinityType;
import eatyourbeets.utilities.JUtils;
import eatyourbeets.utilities.RenderHelpers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class VariableToken extends CTToken
{
    private final char variableID;

    static final Map<Character, VariableToken> tokenCache = new HashMap<>();
    static
    {
        tokenCache.put('D', new VariableToken('D'));
        tokenCache.put('M', new VariableToken('M'));
        tokenCache.put('B', new VariableToken('B'));
        tokenCache.put('S', new VariableToken('S'));
        tokenCache.put('A', new VariableToken('A'));
    }

    private VariableToken(char variableID)
    {
        super(CTTokenType.Variable, null);
        this.variableID = variableID;
    }

    public static int TryAdd(CTContext parser)
    {
        if (parser.character == '!' && parser.CompareNext(2, '!'))
        {
            VariableToken token = tokenCache.get(parser.NextCharacter(1));
            if (token != null)
            {
                parser.AddToken(token);
            }
            else
            {
                JUtils.LogInfo(VariableToken.class, "Unknown variable type: " + parser.text);
            }

            return 3;
        }

        return 0;
    }

    @Override
    protected float GetWidth(BitmapFont font, String text)
    {
        if (text == null)
        {
            return super.GetWidth(font, "_."); //20f * Settings.scale * font.getScaleX(); // AbstractCard.MAGIC_NUM_W
        }
        else
        {
            return super.GetWidth(font, text);
        }
    }

    @Override
    public void Render(SpriteBatch sb, CTContext context)
    {
        if (variableID == 'A')
        {
            int i = 1;
            boolean requireAll = false;
            CTLine line = context.lines.get(context.lineIndex);
            ArrayList<AffinityType> types = new ArrayList<>();
            while (true)
            {
                final CTToken next = line.Get(line.tokenIndex + (i++));
                if (next instanceof SymbolToken)
                {
                    AffinityType t = AffinityType.FromTooltip(((SymbolToken)next).tooltip);
                    if (t != null)
                    {
                        types.add(t);
                    }
                    else
                    {
                        break;
                    }
                }
                else if (next instanceof WordToken)
                {
                    if (next.text.equals("and"))
                    {
                        requireAll = true;
                    }
                    else if (!next.text.equals("or"))
                    {
                        break;
                    }
                }
                else if (!(next instanceof WhitespaceToken))
                {
                    break;
                }
            }

            super.Render(sb, context, context.card.GetAffinityString(types, requireAll));
            return;
        }

        super.Render(sb, context, RenderHelpers.GetCardAttributeString(context.card, variableID));
    }
}