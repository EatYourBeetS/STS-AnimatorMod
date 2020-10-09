package eatyourbeets.cards.base.cardTextParsing;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.cards.base.EYBCardTooltip;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.resources.CardTooltips;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.JUtils;

public class WordToken extends CTToken
{
    protected EYBCardTooltip tooltip = null;
    protected String modifier = "";
    protected Color overrideColor = null;

    protected static boolean IsValidCharacter(Character character, boolean firstCharacter)
    {
        if (character == null)
        {
            return false;
        }
        else if (firstCharacter)
        {
            return Character.isLetterOrDigit(character) || character == '~';
        }
        else
        {
            return Character.isLetterOrDigit(character) || ("_*+-".indexOf(character) >= 0);
        }
    }

    @Override // TODO: improve modifier logic
    public float GetWidth(CTContext context)
    {
        if (modifier.isEmpty())
        {
            return GetWidth(context.font, text);
        }
        else if (modifier.equals("s"))
        {
            if (context.card != null && context.card.magicNumber == 1)
            {
                return GetWidth(context.font, text);
            }

            return GetWidth(context.font, text + "s");
        }
        else
        {
            return GetWidth(context.font, text + "(0)");
        }
    }

    protected WordToken(String text)
    {
        super(CTTokenType.Text, text);
    }

    public static int TryAdd(CTContext parser)
    {
        if (IsValidCharacter(parser.character, true))
        {
            tempBuilder.setLength(0);
            builder.setLength(0);
            builder.append(parser.character);

            int i = 1;
            boolean mod = false;
            boolean skip = false;
            while (true)
            {
                Character next = parser.NextCharacter(i);

                if (next == null)
                {
                    break;
                }
                else if (next == '/')
                {
                    if (parser.card.upgraded)
                    {
                        builder.setLength(0);
                        mod = false;
                    }
                    else
                    {
                        skip = true;
                    }
                }
                else if (next == '(')
                {
                    mod = true;
                }
                else if (mod && next == ')')
                {
                    mod = false;
                }
                else if (IsValidCharacter(next, false))
                {
                    if (!skip)
                    {
                        if (mod)
                        {
                            tempBuilder.append(next);
                        }
                        else
                        {
                            builder.append(next);
                        }
                    }
                }
                else
                {
                    break;
                }

                i += 1;
            }

            String word = builder.toString();

            WordToken token;
            if (word.charAt(0) == '~' && word.length() > 1)
            {
                token = new WordToken(word.substring(1));
            }
            else
            {
                token = new WordToken(word);
            }

            if (parser.card != null)
            {
                EYBCardTooltip tooltip = CardTooltips.FindByName(word.toLowerCase());
                if (tooltip != null)
                {
                    parser.AddTooltip(tooltip);
                    token.overrideColor = Settings.GOLD_COLOR.cpy();
                    token.tooltip = tooltip;
                }
            }

            token.modifier = tempBuilder.toString();
            parser.AddToken(token);

            return i;
        }

        return 0;
    }

    @Override
    public void Render(SpriteBatch sb, CTContext context)
    {
        String text = this.text;
        if (modifier.equals("s")) // pluralize
        {
            // TODO: improve this logic
            if (context.card.magicNumber == 0 || context.card.magicNumber > 1)
            {
                text += "s";
            }
        }

        if (overrideColor != null)
        {
            overrideColor.a = context.card.transparency;

            if (tooltip == GR.Tooltips.Starter && !AbstractDungeon.actionManager.cardsPlayedThisTurn.isEmpty())
            {
                overrideColor.a = context.card.transparency * 0.6f;
            }
            else
            {
                Integer t = null;
                if (tooltip == GR.Tooltips.Limited)
                {
                    t = CombatStats.GetCombatData(context.card.cardID, null);
                }
                else if (tooltip == GR.Tooltips.SemiLimited)
                {
                    t = CombatStats.GetTurnData(context.card.cardID, null);
                }

                if (t != null)
                {
                    if (!modifier.isEmpty())
                    {
                        int n = JUtils.ParseInt(modifier, 0);
                        text += "(" + Math.max(0, n - t) + ")";
                        if (t >= n)
                        {
                            overrideColor.a = context.card.transparency * 0.6f;
                        }
                    }
                    else if (t > 0)
                    {
                        overrideColor.a = context.card.transparency * 0.6f;
                    }
                }
            }

            Render(sb, context, text, overrideColor);
        }
        else
        {
            Render(sb, context, text, context.color);
        }
    }
}
