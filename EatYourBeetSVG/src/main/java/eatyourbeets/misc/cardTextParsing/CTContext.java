package eatyourbeets.misc.cardTextParsing;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.cards.base.EYBCardTooltip;
import eatyourbeets.utilities.JavaUtilities;

import java.util.ArrayList;

public class CTContext
{
    protected final static float IMG_HEIGHT = 420.0F * Settings.scale;
    protected final static float IMG_WIDTH = 300.0F * Settings.scale;
    protected final static float DESC_BOX_WIDTH = Settings.BIG_TEXT_MODE ? IMG_WIDTH * 0.95F : IMG_WIDTH * 0.79F;
    protected final static float DESC_OFFSET_Y = Settings.BIG_TEXT_MODE ? IMG_HEIGHT * 0.24F : IMG_HEIGHT * 0.255F;
    protected final static float CN_DESC_BOX_WIDTH = IMG_WIDTH * 0.72F;
    protected final static Color DEFAULT_COLOR = Settings.CREAM_COLOR.cpy();

    public final ArrayList<EYBCardTooltip> tooltips = new ArrayList<>();
    public final ArrayList<CTLine> lines = new ArrayList<>();

    protected Character character;
    protected BitmapFont font;
    protected CharSequence text;
    protected int remaining;
    protected int characterIndex;
    protected int lineIndex;

    public float whitespaceWidth = 0;
    public EYBCard card;
    public float start_y;
    public float start_x;
    public float spacing;
    public float height;
    public Color color;
    public int line;

    public static void Test()
    {
        CTContext test = new CTContext();

        test.Initialize(null,"Deal !D! ({G:+3.0}[F]) {K:Preview|Entou Jyuu} Piercing NL damage. Apply NL !M! Poison.", FontHelper.cardDescFont_N);
        test.Initialize(null, "At the end of your turn, deal !M! damage to all enemies for each #yDebuff[] they have.", FontHelper.cardDescFont_N);
    }

    public void Initialize(EYBCard card, String text, BitmapFont font)
    {
        this.card = card;
        this.font = font;
        this.lines.clear();
        this.tooltips.clear();
        this.lines.add(new CTLine());
        this.text = text;
        this.characterIndex = 0;
        this.lineIndex = 0;

        int amount = 0;
        while (MoveIndex(amount))
        {
            this.character = this.text.charAt(characterIndex);

            // The order matters!
            if ((amount = VariableToken.TryAdd(this))    == 0 // !M!
            &&  (amount = SymbolToken.TryAdd(this))      == 0 // [E]
            &&  (amount = SpecialToken.TryAdd(this))     == 0 // {code}
            &&  (amount = NewLineToken.TryAdd(this))     == 0 // NL
            &&  (amount = WhitespaceToken.TryAdd(this))  == 0 //
            &&  (amount = PunctuationToken.TryAdd(this)) == 0 // .,-.:; etc
            &&  (amount = WordToken.TryAdd(this))        == 0)// Letters/Digits
            {
                JavaUtilities.GetLogger(this).error("Error parsing card text, Character: " + character + ", Text: " + this.text);
                amount = 1;
            }
        }

        this.lines.get(lineIndex).TrimEnd(); // Remove possible whitespace from the last line
    }

    public void Render(SpriteBatch sb)
    {
        this.line = 0;

        if (card.angle == 0.0F && card.drawScale == 1.0F)
        {
            this.font = FontHelper.cardDescFont_N;
        }
        else
        {
            this.font = FontHelper.cardDescFont_L;
            this.font.getData().setScale(card.drawScale);
        }

        this.height = font.getCapHeight();
        this.spacing = 1.45F * -height / Settings.scale / card.drawScale;
        this.start_y = card.current_y - IMG_HEIGHT * card.drawScale * 0.5f + DESC_OFFSET_Y * card.drawScale + (lines.size() * height) * 0.775F - height * 0.375F;
        this.start_x = 0;
        this.line = 0;
        this.color = DEFAULT_COLOR;
        this.whitespaceWidth = 0;

        for (CTLine line : lines)
        {
            line.Render(sb, this);
        }

        font.getData().setScale(1);
    }

    protected boolean CompareNext(int amount, char character)
    {
        Character other = NextCharacter(amount);
        if (other != null)
        {
            return other == character;
        }

        return false;
    }

    protected Character NextCharacter(int amount)
    {
        if (amount > remaining)
        {
            return null;
        }

        return text.charAt(characterIndex + amount);
    }

    protected boolean MoveIndex(int amount)
    {
        characterIndex += amount;
        remaining = text.length() - characterIndex - 1;

        return remaining >= 0;
    }

    protected void AddToken(CTToken token)
    {
        CTLine line;

        if (token.type == CTTokenType.NewLine)
        {
            line = new CTLine();
            lines.add(line);
            lineIndex += 1;
        }
        else
        {
            line = lines.get(lineIndex);
            token.SetWidth(font);

            if (line.Count() > 0 && (token.width + line.width) > DESC_BOX_WIDTH)
            {
                line.TrimEnd();
                line = new CTLine();
                lines.add(line);
                lineIndex += 1;
            }

            if (line.Count() > 0 || token.type != CTTokenType.Whitespace)
            {
                line.Add(token);
            }
        }
    }

    protected void AddTooltip(EYBCardTooltip tooltip)
    {
        if (!tooltips.contains(tooltip))
        {
            tooltips.add(tooltip);
        }
    }
}
