package eatyourbeets.cards.base.cardTextParsing;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.ColoredString;
import eatyourbeets.utilities.JUtils;
import eatyourbeets.utilities.RenderHelpers;

import java.util.ArrayList;

public class VariableToken extends CTToken
{
    private final char variableID;
    private ColoredString coloredString;

    static final ArrayList<Character> validTokens = new ArrayList<>();
    static
    {
        validTokens.add('D');
        validTokens.add('M');
        validTokens.add('B');
        validTokens.add('S');
        validTokens.add('A');
        validTokens.add('K');
    }

    private static VariableToken TryCreateToken(Character c)
    {
        return validTokens.contains(c) ? new VariableToken(c) : null;
    }

    private VariableToken(char variableID)
    {
        super(CTTokenType.Variable, null);

        this.coloredString = new ColoredString(null, null);
        this.variableID = variableID;
    }

    public static int TryAdd(CTContext parser)
    {
        if (parser.character == '!' && parser.CompareNext(2, '!'))
        {
            final VariableToken token = TryCreateToken(parser.NextCharacter(1));
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
        if (coloredString.text == null || GR.UI.Elapsed25())
        {
            UpdateString(context);
        }

        super.Render(sb, context, coloredString);
    }

    private void UpdateString(CTContext context)
    {
        if (variableID == 'A')
        {
            int i = 1;
            boolean requireAll = true;
            final CTLine line = context.lines.get(context.lineIndex);
            final ArrayList<Affinity> types = new ArrayList<>();
            while (true)
            {
                final CTToken next = line.Get(line.tokenIndex + (i++));
                if (next instanceof SymbolToken)
                {
                    final Affinity t = Affinity.FromTooltip(((SymbolToken) next).tooltip);
                    if (t != null)
                    {
                        types.add(t);
                    }
                    else
                    {
                        break;
                    }
                }
                else if (!(next instanceof WhitespaceToken))
                {
                    break;
                }
            }

            coloredString = context.card.GetAffinityString(types, requireAll);
        }
        else
        {
            coloredString = RenderHelpers.GetCardAttributeString(context.card, variableID);
        }
    }
}