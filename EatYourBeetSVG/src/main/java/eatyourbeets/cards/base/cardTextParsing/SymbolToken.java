package eatyourbeets.cards.base.cardTextParsing;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.cards.base.EYBCardTooltip;
import eatyourbeets.resources.CardTooltips;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.JUtils;

import java.util.HashMap;
import java.util.Map;

public class SymbolToken extends CTToken
{
    protected EYBCardTooltip tooltip;

    public static final Map<String, SymbolToken> tokenCache = new HashMap<>();
    static
    {
//        tokenCache.put("R", new SymbolToken("[R]"));
//        tokenCache.put("G", new SymbolToken("[G]"));
        tokenCache.put("E", new SymbolToken("[E]")); // Energy
        tokenCache.put(Affinity.Red.PowerSymbol, new SymbolToken(Affinity.Red.GetFormattedPowerSymbol()));
        tokenCache.put(Affinity.Green.PowerSymbol, new SymbolToken(Affinity.Green.GetFormattedPowerSymbol()));
        tokenCache.put(Affinity.Blue.PowerSymbol, new SymbolToken(Affinity.Blue.GetFormattedPowerSymbol()));
        tokenCache.put(Affinity.Orange.PowerSymbol, new SymbolToken(Affinity.Orange.GetFormattedPowerSymbol()));
        tokenCache.put(Affinity.Light.PowerSymbol, new SymbolToken(Affinity.Light.GetFormattedPowerSymbol()));
        tokenCache.put(Affinity.Dark.PowerSymbol, new SymbolToken(Affinity.Dark.GetFormattedPowerSymbol()));
        tokenCache.put(Affinity.Silver.PowerSymbol, new SymbolToken(Affinity.Silver.GetFormattedPowerSymbol()));
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

            if (tooltip.backgroundColor != null) {
                sb.setColor(tooltip.backgroundColor);
                sb.draw(GR.Common.Images.Badges.Base_Badge.Texture(), context.start_x - diff * 2.2f, context.start_y - (partial * 6) * 1.2f, iconW * 1.2f, iconH * 1.2f);
            }
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