package eatyourbeets.misc.cardTextParsing;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class WhitespaceToken extends CTToken
{
    protected static final WhitespaceToken Default = new WhitespaceToken();

    private WhitespaceToken()
    {
        super(CTTokenType.Whitespace, " ");
    }

    public static int TryAdd(CTContext parser)
    {
        if (Character.isWhitespace(parser.character))
        {
            parser.AddToken(Default);

            return 1;
        }

        return 0;
    }

    @Override
    public void Render(SpriteBatch sb, CTContext context)
    {
        layout.setText(context.font, text);
        context.start_x += layout.width;
    }
}
