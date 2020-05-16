package eatyourbeets.cards.base.cardTextParsing;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.cards.base.EYBCardTooltip;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.JavaUtilities;

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
        this.tooltip = GR.Tooltips.FindByName(text);
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
                        token = new SymbolToken(GR.Tooltips.FindByID(key));
                        tokenCache.put(key, token);
                    }

                    if (token.tooltip != null)
                    {
                        parser.AddToken(token);
                        parser.AddTooltip(token.tooltip);
                    }
                    else
                    {
                        JavaUtilities.Log(SymbolToken.class, "Unknown symbol type: " + parser.text);
                    }

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
            sb.setColor(context.color);
            sb.draw(tooltip.icon, context.start_x - partial, context.start_y - (partial * 6), size, size);
        }
        else
        {
            JavaUtilities.GetLogger(this).error("tooltip.icon was null, " + tooltip.title);
        }

        context.start_x += (size - partial);
    }
}