package eatyourbeets.misc.cardTextParsing;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class WhitespaceToken extends CTToken
{
    private static final WhitespaceToken Default = new WhitespaceToken(" ");

    public static int TryAdd(CTContext parser)
    {
        if (Character.isWhitespace(parser.character))
        {
            parser.AddToken(Default);

            return 1;
        }

        return 0;
    }

    public WhitespaceToken(Object text)
    {
        this.type = CTTokenType.Whitespace;
        this.text = String.valueOf(text);
    }

    @Override
    public void Render(SpriteBatch sb, CTContext context)
    {
        if (context.whitespaceWidth <= 0)
        {
            layout.setText(context.font, text);
            context.whitespaceWidth = layout.width;
        }

        context.start_x += context.whitespaceWidth;
    }
}
