package pinacolada.cards.base.cardTextParsing;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardTooltip;
import pinacolada.resources.CardTooltips;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLJUtils;

import java.util.HashMap;
import java.util.Map;

public class SymbolToken extends CTToken
{
    protected PCLCardTooltip tooltip;

    public static final Map<String, SymbolToken> tokenCache = new HashMap<>();
    static
    {
//        tokenCache.put("R", new SymbolToken("[R]"));
//        tokenCache.put("G", new SymbolToken("[G]"));
        tokenCache.put("E", new SymbolToken("[E]")); // Energy
        tokenCache.put(PCLAffinity.Red.PowerSymbol, new SymbolToken(PCLAffinity.Red.GetFormattedPowerSymbol()));
        tokenCache.put(PCLAffinity.Green.PowerSymbol, new SymbolToken(PCLAffinity.Green.GetFormattedPowerSymbol()));
        tokenCache.put(PCLAffinity.Blue.PowerSymbol, new SymbolToken(PCLAffinity.Blue.GetFormattedPowerSymbol()));
        tokenCache.put(PCLAffinity.Orange.PowerSymbol, new SymbolToken(PCLAffinity.Orange.GetFormattedPowerSymbol()));
        tokenCache.put(PCLAffinity.Light.PowerSymbol, new SymbolToken(PCLAffinity.Light.GetFormattedPowerSymbol()));
        tokenCache.put(PCLAffinity.Dark.PowerSymbol, new SymbolToken(PCLAffinity.Dark.GetFormattedPowerSymbol()));
        tokenCache.put(PCLAffinity.Silver.PowerSymbol, new SymbolToken(PCLAffinity.Silver.GetFormattedPowerSymbol()));
    }

    private SymbolToken(String text)
    {
        super(CTTokenType.Symbol, text);
        this.tooltip = CardTooltips.FindByName(text);
    }

    private SymbolToken(PCLCardTooltip tooltip)
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
                        final PCLCardTooltip tooltip = CardTooltips.FindByID(key);
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
        PCLCard card = context.card;
        float size = GetWidth(context);// 24f * Settings.scale * card.drawScale * context.scaleModifier;
        float partial = size / 12f;

        if (tooltip.icon != null)
        {
            float iconW = size * tooltip.iconMulti_W;
            float iconH = size * tooltip.iconMulti_H;
            float diff = partial / tooltip.iconMulti_W;

            if (tooltip.backgroundColor != null) {
                sb.setColor(tooltip.backgroundColor);
                sb.draw(GR.PCL.Images.Badges.Base_Badge.Texture(), context.start_x - diff * 2.2f, context.start_y - (partial * 6) * 1.2f, iconW * 1.2f, iconH * 1.2f);
            }
            sb.setColor(context.color);
            sb.draw(tooltip.icon, context.start_x - diff, context.start_y - (partial * 6), iconW, iconH);
        }
        else
        {
            PCLJUtils.LogError(this, "tooltip.icon was null, " + tooltip.title);
        }

        context.start_x += (size - partial);
    }
}