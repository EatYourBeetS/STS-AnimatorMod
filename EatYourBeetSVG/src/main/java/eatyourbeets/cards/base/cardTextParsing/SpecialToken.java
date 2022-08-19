package eatyourbeets.cards.base.cardTextParsing;

import com.megacrit.cardcrawl.core.Settings;
import eatyourbeets.cards.base.EYBCardTooltip;
import eatyourbeets.resources.CardTooltips;

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
            int indentation = 0;
            while (true)
            {
                final Character next = parser.NextCharacter(i);
                if (next == null)
                {
                    break;
                }
                else if (next == '{')
                {
                    indentation += 1;
                }
                else if (next == '}')
                {
                    if (indentation > 0)
                    {
                        indentation -= 1;
                        i += 1;
                        continue;
                    }

                    final String word = builder.toString();
                    EYBCardTooltip tooltip = CardTooltips.FindByName(word
                    .replace(" NL ", " ")
                    .split("\\(")[0] // Ignore modifiers
                    .toLowerCase());

                    if (tooltip != null)
                    {
                        if (tooltip.requiredColor != null && tooltip.requiredColor != parser.resources.CardColor)
                        {
                            tooltip = null;
                        }
                        else
                        {
                            parser.AddTooltip(tooltip);
                        }
                    }

                    if (word.startsWith("~"))
                    {
                        internalParser.Initialize(null, parser.resources, word.substring(1)); // card must be null
                    }
                    else if (word.startsWith("+"))
                    {
                        if (!parser.card.upgraded)
                        {
                            return i + 1;
                        }

                        if (word.length() == 1)
                        {
                            final WordToken plus = new WordToken(word);
                            plus.coloredString.SetColor(Settings.GOLD_COLOR);
                            parser.AddToken(plus);
                            return i + 1;
                        }

                        internalParser.Initialize(null, parser.resources, word.substring(1)); // card must be null
                    }
                    else if (word.startsWith("-"))
                    {
                        if (parser.card.upgraded)
                        {
                            return i + 1;
                        }

                        internalParser.Initialize(null, parser.resources, word.substring(1)); // card must be null
                    }
                    else
                    {
                        internalParser.Initialize(null, parser.resources, word); // card must be null
                    }

                    for (CTToken token : internalParser.lines.get(0).tokens) // All the tokens are in the first line, regardless of width and type
                    {
                        if (token instanceof WordToken)
                        {
                            ((WordToken)token).coloredString.SetColor(Settings.GOLD_COLOR);
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