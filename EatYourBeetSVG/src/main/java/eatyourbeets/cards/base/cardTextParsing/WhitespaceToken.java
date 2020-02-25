package eatyourbeets.cards.base.cardTextParsing;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;

public class WhitespaceToken extends CTToken
{
    protected static final WhitespaceToken Default = new WhitespaceToken();

    private WhitespaceToken()
    {
        super(CTTokenType.Whitespace, " ");
    }

    @Override
    protected float GetWidth(BitmapFont font, String text)
    {
        BitmapFont.BitmapFontData data = font.getData();
        if (Settings.language == Settings.GameLanguage.ZHS)
        {
            return data.scaleX * data.spaceWidth * text.length() * 0.4f;
        }
        else
        {
            return data.scaleX * data.spaceWidth * text.length();// super.GetWidth(font, text);
        }
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
        context.start_x += GetWidth(context.font, text);
    }
}
