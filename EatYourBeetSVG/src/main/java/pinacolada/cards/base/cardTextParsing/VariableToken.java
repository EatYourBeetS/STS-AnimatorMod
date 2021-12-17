package pinacolada.cards.base.cardTextParsing;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import eatyourbeets.utilities.ColoredString;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLJUtils;
import pinacolada.utilities.PCLRenderHelpers;

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
        validTokens.add('C');
        validTokens.add('K');
        validTokens.add('X');
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
                PCLJUtils.LogInfo(VariableToken.class, "Unknown variable type: " + parser.text);
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
        if (coloredString == null || coloredString.text == null || GR.UI.Elapsed25())
        {
            UpdateString(context);
        }

        super.Render(sb, context, coloredString);
    }

    private void UpdateString(CTContext context)
    {
        if (variableID == 'A')
        {
            boolean requireAll = true;
            final CTLine line = context.lines.get(context.lineIndex);
            final ArrayList<PCLAffinity> types = new ArrayList<>();
            int i = line.tokenIndex + 1;
            while (i < line.tokens.size())
            {
                final CTToken next = line.Get((i++));
                if (next instanceof SymbolToken)
                {
                    PCLAffinity t = PCLAffinity.FromTooltip(((SymbolToken) next).tooltip);
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
                    if (next.rawText.equals("or"))
                    {
                        requireAll = false;
                    }
                    else if (!next.rawText.equals("and"))
                    {
                        break;
                    }
                }
                else if (!(next instanceof WhitespaceToken) && next != null)
                {
                    break;
                }
            }

            coloredString = context.card.GetAffinityString(types, requireAll);
        }
        else
        {
            coloredString = PCLRenderHelpers.GetCardAttributeString(context.card, variableID);
        }
    }
}