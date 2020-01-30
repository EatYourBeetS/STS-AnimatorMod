package eatyourbeets.cards.base.cardTextParsing;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import eatyourbeets.cards.base.EYBCardTooltip;
import eatyourbeets.resources.GR;

public class WordToken extends CTToken
{
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
                else if (IsValidCharacter(next, false))
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
                token.overrideColor = Settings.GOLD_COLOR.cpy();
            }

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
            overrideColor.a = context.card.targetTransparency;

            Render(sb, context, text, overrideColor);
        }
        else
        {
            Render(sb, context, text, context.color);
        }
    }
}
