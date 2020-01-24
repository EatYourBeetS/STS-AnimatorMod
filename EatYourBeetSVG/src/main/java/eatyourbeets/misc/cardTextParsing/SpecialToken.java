package eatyourbeets.misc.cardTextParsing;

import com.megacrit.cardcrawl.core.Settings;
import eatyourbeets.cards.base.EYBCardTooltip;
import eatyourbeets.resources.GR;

public class SpecialToken extends CTToken
{
    private static final CTContext internalParser = new CTContext();

    private SpecialToken()
    {
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

                    EYBCardTooltip tooltip = GR.GetTooltip(word.toLowerCase());
                    if (tooltip != null)
                    {
                        parser.AddTooltip(tooltip);
                    }

                    if (word.length() > 1 && word.charAt(0) == '~')
                    {
                        internalParser.Initialize(null, word.substring(1), parser.font);
                    }
                    else
                    {
                        internalParser.Initialize(null, word, parser.font);
                    }

                    for (CTLine line : internalParser.lines)
                    {
                        for (CTToken token : line.tokens)
                        {
                            if (token instanceof WordToken)
                            {
                                ((WordToken)token).overrideColor = Settings.GOLD_COLOR;
                            }

                            parser.AddToken(token);
                        }
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