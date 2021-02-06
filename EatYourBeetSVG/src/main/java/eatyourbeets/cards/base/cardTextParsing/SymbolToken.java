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
        tokenCache.put("R", new SymbolToken("[R]"));
        tokenCache.put("G", new SymbolToken("[G]"));
        tokenCache.put("B", new SymbolToken("[B]"));
        tokenCache.put("W", new SymbolToken("[W]"));
        tokenCache.put("E", new SymbolToken("[E]")); // Energy
        tokenCache.put("F", new SymbolToken("[F]")); // Force
        tokenCache.put("A", new SymbolToken("[A]")); // Agility
        tokenCache.put("I", new SymbolToken("[I]")); // Intellect
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
                    String key = builder.toString();
                    SymbolToken token = tokenCache.get(key);
                    if (token == null)
                    {
                        EYBCardTooltip tooltip = CardTooltips.FindByID(key);
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