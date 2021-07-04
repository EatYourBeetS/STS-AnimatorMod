package eatyourbeets.cards.base;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.cards.AbstractCard;
import eatyourbeets.utilities.JUtils;
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

    public void Render(SpriteBatch sb, float x, float y, float size)
    {
        RenderHelpers.Draw(sb, Type.GetIcon(), x, y, size);

        Texture border = Type.GetBorder(level);
        if (border != null)
        {
            RenderHelpers.Draw(sb, border, x, y, size);
        }
    }

    public void RenderOnCard(SpriteBatch sb, AbstractCard card, float x, float y, float size)
    {
        RenderHelpers.DrawOnCardAuto(sb, card, Type.GetIcon(), new Vector2(x, y), size, size, Color.WHITE, 1f, 1f);

        Texture border = Type.GetBorder(level);
        if (border != null)
        {
            Color color = Color.WHITE.cpy();
            EYBCardBase c = JUtils.SafeCast(card, EYBCardBase.class);
            if (c != null && level > 1)
            {
                color.lerp(c.GetRarityColor(false), 0.3f);
            }

            RenderHelpers.DrawOnCardAuto(sb, card, border, new Vector2(x, y), size, size, color, 1f, 1f);
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