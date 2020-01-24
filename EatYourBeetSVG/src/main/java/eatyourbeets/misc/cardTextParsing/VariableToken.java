package eatyourbeets.misc.cardTextParsing;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.megacrit.cardcrawl.core.Settings;
import eatyourbeets.utilities.JavaUtilities;

import java.util.HashMap;
import java.util.Map;

public class VariableToken extends CTToken
{
    static final Map<Character, VariableToken> tokenCache = new HashMap<>();
    static
    {
        tokenCache.put('D', new VariableToken("D"));
        tokenCache.put('M', new VariableToken("M"));
        tokenCache.put('B', new VariableToken("B"));
        tokenCache.put('S', new VariableToken("S"));
    }

    public VariableToken(Object text)
    {
        this.type = CTTokenType.Variable;
        this.text = String.valueOf(text);
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
    public void SetWidth(BitmapFont font)
    {
        this.width = 20f * Settings.scale; // AbstractCard.MAGIC_NUM_W
    }
}