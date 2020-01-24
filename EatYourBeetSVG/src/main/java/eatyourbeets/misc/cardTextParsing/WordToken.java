package eatyourbeets.misc.cardTextParsing;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import eatyourbeets.cards.base.EYBCardTooltip;
import eatyourbeets.resources.GR;

public class WordToken extends CTToken
{
    protected Color overrideColor = null;

    protected static boolean IsValidCharacter(Character character)
    {
        return character != null && ("~_*".indexOf(character) >= 0 || (Character.isLetterOrDigit(character)));
    }

    protected WordToken(Object text)
    {
        this.text = String.valueOf(text);
        this.type = CTTokenType.Text;
    }

    public static int TryAdd(CTContext parser)
    {
        if (IsValidCharacter(parser.character))
        {
            builder.setLength(0);
            builder.append(parser.character);

            int i = 1;
            while (true)
            {
                Character next = parser.NextCharacter(i);

                if (IsValidCharacter(next))
                {
                    builder.append(next);
                }
                else
                {
                    String word = builder.toString();
                    WordToken token = new WordToken(word);
                    parser.AddToken(token);

                    EYBCardTooltip tooltip = GR.GetTooltip(word.toLowerCase());
                    if (tooltip != null)
                    {
                        parser.AddTooltip(tooltip);
                        token.overrideColor = Settings.GOLD_COLOR;
                    }

                    return i;
                }

                i += 1;
            }
        }

        return 0;
    }

    @Override
    public void Render(SpriteBatch sb, CTContext context)
    {
        if (overrideColor != null)
        {
            context.color = overrideColor;

            super.Render(sb, context);

            context.color = CTContext.DEFAULT_COLOR;
        }
        else
        {
            super.Render(sb, context);
        }
    }
}
