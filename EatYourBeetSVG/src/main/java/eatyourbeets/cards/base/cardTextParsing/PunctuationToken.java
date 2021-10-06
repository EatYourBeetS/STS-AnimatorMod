package eatyourbeets.cards.base.cardTextParsing;

import java.util.HashMap;
import java.util.Map;

public class PunctuationToken extends CTToken
{
    private static Map<String, PunctuationToken> tokenCache = new HashMap<>();

    protected PunctuationToken(String text)
    {
        super(CTTokenType.Punctuation, text);
    }

    protected static boolean IsValidCharacter(Character character, boolean firstCharacter)
    {
        if (character == null)
        {
            return false;
        }
        else if (firstCharacter)
        {
            return !Character.isLetterOrDigit(character) && !Character.isWhitespace(character) && "<>".indexOf(character) == -1;
        }
        else
        {
            return ("{[!#<_*@>]}".indexOf(character) == -1) && !Character.isLetterOrDigit(character) && !Character.isWhitespace(character);
        }
    }

    public static int TryAdd(CTContext parser)
    {
        if (IsValidCharacter(parser.character, true))
        {
            builder.setLength(0);
            builder.append(parser.character);

            int i = 1;
            while (true)
            {
                Character next = parser.NextCharacter(i);

                if (IsValidCharacter(next, false))
                {
                    builder.append(next);
                }
                else
                {
                    PunctuationToken token;
                    String text = builder.toString();
                    if (text.length() < 4)
                    {
                        token = tokenCache.get(text);
                        if (token == null)
                        {
                            tokenCache.put(text, (token = new PunctuationToken(text)));
                        }
                    }
                    else
                    {
                        token = new PunctuationToken(text);
                    }

                    parser.AddToken(token);

                    return i;
                }

                i += 1;
            }
        }

        return 0;
    }
}