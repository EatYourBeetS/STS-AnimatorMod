package eatyourbeets.cards.base.cardTextParsing;

import com.megacrit.cardcrawl.core.Settings;
import eatyourbeets.cards.base.EYBCardTooltip;
import eatyourbeets.resources.GR;

public abstract class SpecialToken extends CTToken
{
    private static final CTContext internalParser = new CTContext();

    private SpecialToken()
    {
        super(null, null);
        // this could be:
        // {#G:+4.0} -> Green colored text
        // {kw(Preview):Entou Jyuu} -> Add 'Preview' keyword, but show gold colored 'Entou Jyuu'
        // {kw():Channel} -> do not parse keyword
        // {Attack} -> Use gold color by default
        // {#:Text} -> Reset to default color
    }

    public static int TryAdd(CTContext parser)
    {
        if (parser.character == '{' && parser.remaining > 1)
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
                else if (next == '}')
                {
                    String word = builder.toString();

                    EYBCardTooltip tooltip = GR.Tooltips.FindByName(word.toLowerCase());
                    if (tooltip != null)
                    {
                        parser.AddTooltip(tooltip);
                    }

                    if (word.startsWith("~"))
                    {
                        internalParser.Initialize(null, word.substring(1)); // card must be null
                    }
                    else if (word.startsWith("+"))
                    {
                        if (!parser.card.upgraded)
                        {
                            return i + 1;
                        }

                        internalParser.Initialize(null, word.substring(1)); // card must be null
                    }
                    else if (word.startsWith("-"))
                    {
                        if (parser.card.upgraded)
                        {
                            return i + 1;
                        }

                        internalParser.Initialize(null, word.substring(1)); // card must be null
                    }
                    else
                    {
                        internalParser.Initialize(null, word); // card must be null
                    }

                    for (CTToken token : internalParser.lines.get(0).tokens) // All the tokens are in the first line, regardless of width and type
                    {
                        if (token instanceof WordToken)
                        {
                            ((WordToken)token).overrideColor = Settings.GOLD_COLOR.cpy();
                            ((WordToken)token).tooltip = tooltip;
                        }

                        parser.AddToken(token);
                    }

                    return i + 1;
                }
                else
                {
                    builder.append(next);
                }

                i += 1;
            }
        }

        return 0;
    }
}