package eatyourbeets.misc.cardTextParsing;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.cards.base.EYBCardTooltip;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.JavaUtilities;

import java.util.HashMap;
import java.util.Map;

public class SymbolToken extends CTToken
{
    protected EYBCardTooltip tooltip;

    static final Map<Character, SymbolToken> tokenCache = new HashMap<>();
    static
    {
        tokenCache.put('R', new SymbolToken("[R]"));
        tokenCache.put('G', new SymbolToken("[G]"));
        tokenCache.put('B', new SymbolToken("[B]"));
        tokenCache.put('W', new SymbolToken("[W]"));
        tokenCache.put('E', new SymbolToken("[E]")); // Energy
        tokenCache.put('F', new SymbolToken("[F]")); // Force
        tokenCache.put('A', new SymbolToken("[A]")); // Agility
        tokenCache.put('I', new SymbolToken("[I]")); // Intellect
    }

    private SymbolToken(String text)
    {
        super(CTTokenType.Symbol, text);
        tooltip = GR.GetTooltip(text);
    }

    @Override
    protected float GetWidth(BitmapFont font, String text)
    {
        return 24f * Settings.scale * font.getScaleX();// AbstractCard.CARD_ENERGY_IMG_WIDTH
    }

    public static int TryAdd(CTContext parser)
    {
        if (parser.character == '[' && parser.CompareNext(2, ']'))
        {
            SymbolToken token = tokenCache.get(parser.NextCharacter(1));
            if (token != null)
            {
                parser.AddToken(token);
                parser.AddTooltip(token.tooltip);
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
    public void Render(SpriteBatch sb, CTContext context)
    {
        EYBCard card = context.card;
        float size = 24f * Settings.scale * card.drawScale;
        float partial = size / 12f;

        sb.setColor(context.color);
        sb.draw(tooltip.icon, context.start_x - partial, context.start_y - (partial * 6), size, size);

        context.start_x += (size - partial);
    }
}