package eatyourbeets.misc.cardTextParsing;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.megacrit.cardcrawl.core.Settings;
import eatyourbeets.utilities.JavaUtilities;

import java.util.HashMap;
import java.util.Map;

public class SymbolToken extends CTToken
{
    static final Map<Character, SymbolToken> tokenCache = new HashMap<>();
    static
    {
        tokenCache.put('R', new SymbolToken("R"));
        tokenCache.put('G', new SymbolToken("G"));
        tokenCache.put('B', new SymbolToken("B"));
        tokenCache.put('W', new SymbolToken("W"));
        tokenCache.put('E', new SymbolToken("E")); // Energy
        tokenCache.put('F', new SymbolToken("F")); // Force
        tokenCache.put('A', new SymbolToken("A")); // Agility
        tokenCache.put('I', new SymbolToken("I")); // Intellect
    }

    protected SymbolToken(Object text)
    {
        this.type = CTTokenType.Symbol;
        this.text = String.valueOf(text);
    }

    public static int TryAdd(CTContext parser)
    {
        if (parser.character == '[' && parser.CompareNext(2, ']'))
        {
            SymbolToken token = tokenCache.get(parser.NextCharacter(1));
            if (token != null)
            {
                parser.AddToken(token);
            }
            else
            {
                JavaUtilities.Log(SymbolToken.class, "Unknown symbol type: " + parser.text);
            }

            return 3;
        }

        return 0;
    }

    @Override
    public void SetWidth(BitmapFont font)
    {
        this.width = 24f * Settings.scale; // AbstractCard.CARD_ENERGY_IMG_WIDTH
    }
}