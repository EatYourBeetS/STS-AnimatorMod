package eatyourbeets.cards.base.cardTextParsing;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import eatyourbeets.cards.animator.special.ThrowingKnife;
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

        tokenCache.put("T-Knife", new SymbolToken(GR.Common.Images.Tooltips.ThrowingKnife.Texture(), ThrowingKnife.DATA.Strings.NAME));
        tokenCache.put("Gold", new SymbolToken(ImageMaster.UI_GOLD, "Gold"));
        tokenCache.put("GoldPouch", new SymbolToken(ImageMaster.TP_GOLD, "Gold"));
        tokenCache.put("CARD", new SymbolToken(AbstractCard.orb_card, "Card"));
        tokenCache.put("RELIC", new SymbolToken(AbstractCard.orb_relic, "Relic"));
        tokenCache.put("POTION", new SymbolToken(AbstractCard.orb_potion, "Potion"));
        tokenCache.put("SPECIAL", new SymbolToken(AbstractCard.orb_special, null));
    }

    // TODO: Move the key to icon relation somewhere else
    public static TextureRegion GetIcon(String key)
    {
        SymbolToken token = TryGetToken(key);
        if (token.tooltip != null)
        {
            return token.tooltip.icon;
        }

        return null;
    }

    protected static SymbolToken TryGetToken(String key)
    {
        SymbolToken token = tokenCache.get(key);
        if (token == null)
        {
            token = new SymbolToken(GR.Tooltips.FindByID(key));
            tokenCache.put(key, token);
        }

        return token;
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

    private SymbolToken(Texture texture, String title)
    {
        super(CTTokenType.Symbol, "");
        this.tooltip = new EYBCardTooltip(title, null);
        this.tooltip.SetIcon(texture, 6);
    }

    private SymbolToken(TextureRegion icon, String title)
    {
        super(CTTokenType.Symbol, "");
        this.tooltip = new EYBCardTooltip(title, null);
        this.tooltip.icon = icon;
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