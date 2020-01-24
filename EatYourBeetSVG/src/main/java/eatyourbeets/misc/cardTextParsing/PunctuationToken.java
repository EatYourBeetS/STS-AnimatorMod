package eatyourbeets.misc.cardTextParsing;

import java.util.HashMap;
import java.util.Map;

public class PunctuationToken extends CTToken
{
    private static Map<String, PunctuationToken> tokenCache = new HashMap<>();

    protected PunctuationToken(Object text)
    {
        this.type = CTTokenType.Punctuation;
        this.text = String.valueOf(text);
    }

    protected static boolean IsValidCharacter(Character character)
    {
        return character != null && !Character.isLetterOrDigit(character) && "{[!#< _*>]}".indexOf(character) == -1;
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