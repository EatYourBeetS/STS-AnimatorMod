package eatyourbeets.cards.base.cardTextParsing;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.cards.base.EYBCardTooltip;
import eatyourbeets.resources.CardTooltips;
import eatyourbeets.utilities.JUtils;

import java.util.HashMap;
import java.util.Map;

public class SymbolToken extends CTToken
{
    protected EYBCardTooltip tooltip;

    static final Map<String, SymbolToken> tokenCache = new HashMap<>();
    static
    {
//        tokenCache.put("R", new SymbolToken("[R]"));
//        tokenCache.put("G", new SymbolToken("[G]"));
        tokenCache.put("EN", new SymbolToken("[EN]")); // Energy
        tokenCache.put("F", new SymbolToken("[F]")); // Fire
        tokenCache.put("A", new SymbolToken("[A]")); // Air
        tokenCache.put("M", new SymbolToken("[M]")); // Mind
        tokenCache.put("E", new SymbolToken("[E]")); // Earth
        tokenCache.put("L", new SymbolToken("[L]")); // Light
        tokenCache.put("D", new SymbolToken("[D]")); // Dark
        tokenCache.put("W", new SymbolToken("[W]")); // Water
        tokenCache.put("P", new SymbolToken("[P]")); // Poison
        tokenCache.put("S", new SymbolToken("[S]")); // Steel
        tokenCache.put("T", new SymbolToken("[T]")); // Thunder
        tokenCache.put("N", new SymbolToken("[N]")); // Nature
        tokenCache.put("C", new SymbolToken("[C]")); // Cyber
    }

    private SymbolToken(String text)
    {
        super(CTTokenType.Symbol, text);
        this.tooltip = CardTooltips.FindByName(text);
    }

    private SymbolToken(EYBCardTooltip tooltip)
    {
        super(CTTokenType.Symbol, tooltip.title);
        this.tooltip = tooltip;
    }

    @Override
    protected float GetWidth(BitmapFont font, String text)
    {
        return font.getLineHeight() * 0.8f;// AbstractCard.CARD_ENERGY_IMG_WIDTH
    }

    public static int TryAdd(CTContext parser)
    {
        if (parser.character == '[' && parser.remaining > 1)
        {
            builder.setLength(0);

            int i = 1;
            while (true)
            {
                Character next = parser.NextCharacter(i);
                if (next == null)
                {
                    break;
                }
                else if (next == ']')
                {
                    final String key = builder.toString();
                    SymbolToken token = tokenCache.get(key);
                    if (token == null)
                    {
                        final EYBCardTooltip tooltip = CardTooltips.FindByID(key);
                        if (tooltip != null)
                        {
                            token = new SymbolToken(tooltip);
                            tokenCache.put(key, token);
                        }
                        else
                        {
                            throw new RuntimeException("Unknown symbol type: [" + key + "], Raw text is: " + parser.text);
                        }
                    }

                    parser.AddToken(token);
                    parser.AddTooltip(token.tooltip);

                    return i + 1;
                }
                else
                {
                    builder.append(next);
                    i += 1;
                }
            }
        }

        return 0;
    }

    @Override
    public void Render(SpriteBatch sb, CTContext context)
    {
        EYBCard card = context.card;
        float size = GetWidth(context);// 24f * Settings.scale * card.drawScale * context.scaleModifier;
        float partial = size / 12f;

        if (tooltip.icon != null)
        {
            float iconW = size * tooltip.iconMulti_W;
            float iconH = size * tooltip.iconMulti_H;
            float diff = partial / tooltip.iconMulti_W;

            sb.setColor(context.color);
            sb.draw(tooltip.icon, context.start_x - diff, context.start_y - (partial * 6), iconW, iconH);
        }
        else
        {
            JUtils.LogError(this, "tooltip.icon was null, " + tooltip.title);
        }

        context.start_x += (size - partial);
    }
}