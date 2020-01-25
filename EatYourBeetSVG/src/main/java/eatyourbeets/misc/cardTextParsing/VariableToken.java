package eatyourbeets.misc.cardTextParsing;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.utilities.JavaUtilities;

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
                JavaUtilities.Log(VariableToken.class, "Unknown variable type: " + parser.text);
            }

            return 3;
        }

        return 0;
    }

    @Override
    public float GetWidth(BitmapFont font)
    {
        if (text == null)
        {
            return 20f * Settings.scale * font.getScaleX(); // AbstractCard.MAGIC_NUM_W
        }
        else
        {
            return super.GetWidth(font);
        }
    }

    @Override
    public void Render(SpriteBatch sb, CTContext context)
    {
        EYBCard card = context.card;
        switch (variableID)
        {
            case 'D':
            {
                if (card.isDamageModified)
                {
                    context.color = (card.damage >= card.baseDamage) ? Settings.GREEN_TEXT_COLOR : Settings.RED_TEXT_COLOR;
                    text = Integer.toString(card.damage);
                }
                else
                {
                    text = Integer.toString(card.baseDamage);
                }

                break;
            }

            case 'M':
            {
                if (card.isMagicNumberModified)
                {
                    context.color = (card.magicNumber >= card.baseMagicNumber) ? Settings.GREEN_TEXT_COLOR : Settings.RED_TEXT_COLOR;
                    text = Integer.toString(card.magicNumber);
                }
                else
                {
                    text = Integer.toString(card.baseMagicNumber);
                }

                break;
            }

            case 'B':
            {
                if (card.isBlockModified)
                {
                    context.color = (card.block >= card.baseBlock) ? Settings.GREEN_TEXT_COLOR : Settings.RED_TEXT_COLOR;
                    text = Integer.toString(card.block);
                }
                else
                {
                    text = Integer.toString(card.baseBlock);
                }

                break;
            }

            case 'S':
            {
                if (card.isSecondaryValueModified)
                {
                    context.color = (card.secondaryValue > card.baseSecondaryValue) ? Settings.GREEN_TEXT_COLOR : Settings.RED_TEXT_COLOR;
                    text = Integer.toString(card.secondaryValue);
                }
                else
                {
                    text = Integer.toString(card.baseSecondaryValue);
                }

                break;
            }

            default:
            {
                context.color = Settings.RED_TEXT_COLOR;
                text = "?";

                break;
            }
        }

        super.Render(sb, context);

        context.color = CTContext.DEFAULT_COLOR;
    }
}