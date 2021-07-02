package eatyourbeets.cards.base;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.cards.AbstractCard;
import eatyourbeets.utilities.RenderHelpers;

public class EYBCardAlignment implements Comparable<EYBCardAlignment>
{
    public final EYBCardAlignmentType Type;
    public int level;

    public EYBCardAlignment(EYBCardAlignmentType type)
    {
        this.Type = type;
    }

    public EYBCardAlignment(EYBCardAlignmentType type, int level)
    {
        this.Type = type;
        this.level = level;
    }

    public void RenderOnCard(SpriteBatch sb, AbstractCard card, float x, float y, float size)
    {
        RenderHelpers.DrawOnCardAuto(sb, card, Type.GetIcon(), new Vector2(x, y), size, size);

        Texture border = Type.GetBorder(level);
        if (border != null)
        {
            RenderHelpers.DrawOnCardAuto(sb, card, border, new Vector2(x, y), size, size);
        }
    }

    @Override
    public int compareTo(EYBCardAlignment other)
    {
        return other.calculateRank() - calculateRank();
    }

    public int calculateRank()
    {
        if (Type == EYBCardAlignmentType.Star)
        {
            return 500 + level;
        }

        return (level * 100) + (10 - Type.ID);
    }
}