package eatyourbeets.misc.cardTextParsing;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import eatyourbeets.cards.base.EYBCardTooltip;
import eatyourbeets.resources.GR;

public class WordToken extends CTToken
{
    protected String modifier = "";
    protected Color overrideColor = null;

    protected static boolean IsValidCharacter(Character character)
    {
        return character != null && ("~_*".indexOf(character) >= 0 || (Character.isLetterOrDigit(character)));
    }

    protected WordToken(String text)
    {
        super(CTTokenType.Text, text);
    }

    public static int TryAdd(CTContext parser)
    {
        if (IsValidCharacter(parser.character))
        {
            tempBuilder.setLength(0);
            builder.setLength(0);
            builder.append(parser.character);

            int i = 1;
            boolean mod = false;
            while (true)
            {
                Character next = parser.NextCharacter(i);

                if (next == null)
                {
                    break;
                }
                else if (next == '(')
                {
                    mod = true;
                }
                else if (mod && next == ')')
                {
                    mod = false;
                }
                else if (IsValidCharacter(next))
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

            parser.AddToken(token);
            token.modifier = tempBuilder.toString();

            EYBCardTooltip tooltip = GR.GetTooltip(word.toLowerCase());
            if (tooltip != null)
            {
                parser.AddTooltip(tooltip);
                token.overrideColor = Settings.GOLD_COLOR;
            }

            return i;
        }

        return 0;
    }

    @Override
    public void Render(SpriteBatch sb, CTContext context)
    {
        String originalText = null;
        if (modifier.equals("s")) // pluralize
        {
            // TODO: improve this logic
            originalText = text;
            if (context.card.magicNumber == 0 || context.card.magicNumber > 1)
            {
                text = originalText + "s";
            }
        }

        if (overrideColor != null)
        {
            context.color = overrideColor;
        }

        super.Render(sb, context);

        context.color = CTContext.DEFAULT_COLOR;

        if (originalText != null)
        {
            text = originalText;
        }
    }
}
