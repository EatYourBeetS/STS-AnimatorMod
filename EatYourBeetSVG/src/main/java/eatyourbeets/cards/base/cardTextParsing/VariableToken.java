package eatyourbeets.cards.base.cardTextParsing;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import eatyourbeets.cards.base.AffinityType;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.ColoredString;
import eatyourbeets.utilities.JUtils;
import eatyourbeets.utilities.RenderHelpers;

import java.util.ArrayList;
import java.util.Collections;

public class VariableToken extends CTToken
{
    private final char variableID;
    private boolean modifier;
    private ColoredString coloredString;

    static final ArrayList<Character> validTokens = new ArrayList<>();
    static
    {
        validTokens.add('D');
        validTokens.add('M');
        validTokens.add('B');
        validTokens.add('S');
        validTokens.add('A');
    }

    private VariableToken(char variableID)
    {
        super(CTTokenType.Variable, null);
        this.coloredString = new ColoredString(null, null);
        this.variableID = variableID;
    }

    public static int TryAdd(CTContext parser)
    {
        if (parser.character == '!' && parser.remaining > 1)
        {
            int size = 0;
            VariableToken token = null;
            if (parser.CompareNext(1, '~') && parser.CompareNext(3, '!'))
            {
                size = 4;
                token = TryCreateToken(parser.NextCharacter(2));
            }
            else if (parser.CompareNext(2, '!'))
            {
                size = 3;
                token = TryCreateToken(parser.NextCharacter(1));
            }

            if (size > 0)
            {
                if (token != null)
                {
                    token.modifier = (size == 4);
                    parser.AddToken(token);
                }
                else
                {
                    JUtils.LogInfo(VariableToken.class, "Unknown variable type: " + parser.text);
                }

                return size;
            }
        }

        return 0;
    }

    private static VariableToken TryCreateToken(Character c)
    {
        return validTokens.contains(c) ? new VariableToken(c) : null;
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
            boolean requireAll = false;
            CTLine line = context.lines.get(context.lineIndex);
            ArrayList<AffinityType> types = new ArrayList<>();

            if (modifier)
            {
                Collections.addAll(types, AffinityType.BasicTypes());
                coloredString = context.card.GetAffinityString(types, requireAll);
                return;
            }

            while (true)
            {
                final CTToken next = line.Get(line.tokenIndex + (i++));
                if (next instanceof SymbolToken)
                {
                    AffinityType t = AffinityType.FromTooltip(((SymbolToken) next).tooltip);
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
                    if (next.rawText.equals("and"))
                    {
                        requireAll = true;
                    }
                    else if (!next.rawText.equals("or") && !next.rawText.equals(","))
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