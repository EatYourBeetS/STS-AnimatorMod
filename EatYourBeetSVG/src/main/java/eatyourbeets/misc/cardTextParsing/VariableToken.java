package eatyourbeets.misc.cardTextParsing;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import eatyourbeets.utilities.JavaUtilities;
import eatyourbeets.utilities.RenderHelpers;

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
    protected float GetWidth(BitmapFont font, String text)
    {
        if (text == null)
        {
            return 20f * Settings.scale * font.getScaleX(); // AbstractCard.MAGIC_NUM_W
        }
        else
        {
            return super.GetWidth(font, text);
        }
    }

    @Override
    public void Render(SpriteBatch sb, CTContext context)
    {
        super.Render(sb, context, RenderHelpers.GetCardAttributeString(context.card, variableID));
    }
}