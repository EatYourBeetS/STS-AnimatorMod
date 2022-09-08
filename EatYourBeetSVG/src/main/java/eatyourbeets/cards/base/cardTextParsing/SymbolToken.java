package eatyourbeets.cards.base.cardTextParsing;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
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

    protected static class Container
    {
        protected AbstractPlayer.PlayerClass playerClass;
        protected Map<String, SymbolToken> map = new HashMap<>();

        protected Container(AbstractPlayer.PlayerClass playerClass)
        {
            this.playerClass = playerClass;
        }
    }

    static final Map<AbstractPlayer.PlayerClass, Container> tokenCache = new HashMap<>();
    static
    {
        final Container animator = new Container(GR.Animator.PlayerClass);
        animator.map.put("R", new SymbolToken(animator.playerClass, "[R]"));
        animator.map.put("G", new SymbolToken(animator.playerClass, "[G]"));
        animator.map.put("B", new SymbolToken(animator.playerClass, "[B]"));
        animator.map.put("L", new SymbolToken(animator.playerClass, "[L]"));
        animator.map.put("D", new SymbolToken(animator.playerClass, "[D]"));
        animator.map.put("M", new SymbolToken(animator.playerClass, "[M]"));
        animator.map.put("W", new SymbolToken(animator.playerClass, "[W]"));
        animator.map.put("S", new SymbolToken(animator.playerClass, "[S]"));
        final Container animatorClassic = new Container(GR.AnimatorClassic.PlayerClass);
        animatorClassic.map.put("F", new SymbolToken(animatorClassic.playerClass, "[F]"));
        animatorClassic.map.put("A", new SymbolToken(animatorClassic.playerClass, "[A]"));
        animatorClassic.map.put("I", new SymbolToken(animatorClassic.playerClass, "[I]"));
        animatorClassic.map.put("B", new SymbolToken(animatorClassic.playerClass, "[B]"));
        animatorClassic.map.put("C", new SymbolToken(animatorClassic.playerClass, "[C]"));
        final Container unnamed = new Container(GR.Unnamed.PlayerClass);
        final SymbolToken energyToken = new SymbolToken(null, "[E]");
        animator.map.put("E", energyToken);
        animatorClassic.map.put("E", energyToken);
        unnamed.map.put("E", energyToken);

        tokenCache.put(animator.playerClass, animator);
        tokenCache.put(animatorClassic.playerClass, animatorClassic);
        tokenCache.put(unnamed.playerClass, unnamed);
    }

    private SymbolToken(AbstractPlayer.PlayerClass playerClass, String text)
    {
        super(CTTokenType.Symbol, text);

        this.tooltip = CardTooltips.FindByName(playerClass, text);
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
                    final Container container = tokenCache.get(parser.resources.PlayerClass);
                    SymbolToken token = container.map.get(key);
                    if (token == null)
                    {
                        final EYBCardTooltip tooltip = CardTooltips.FindByID(container.playerClass, key);
                        if (tooltip != null)
                        {
                            token = new SymbolToken(tooltip);
                            container.map.put(key, token);
                        }
                        else
                        {
                            throw new RuntimeException((parser.card != null ? parser.card.cardID : "?") + ") Unknown symbol type: [" + key + "], Raw text is: " + parser.text);
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