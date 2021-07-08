package eatyourbeets.cards.base;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.cards.AbstractCard;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.JUtils;
import eatyourbeets.utilities.RenderHelpers;

public class EYBCardAffinity implements Comparable<EYBCardAffinity>
{
    public final AffinityType Type;
    public int level;

    public EYBCardAffinity(AffinityType type)
    {
        this.Type = type;
    }

    public EYBCardAffinity(AffinityType type, int level)
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
        float rotation = 0f;
        Color color = Color.WHITE.cpy();

        if (level > 1)
        {
            EYBCardBase c = JUtils.SafeCast(card, EYBCardBase.class);

            if (c != null)
            {
                rotation = GR.UI.Time_Multi(-(c.isPopup ? 20 : 40));
                color.lerp(c.GetRarityColor(false), 0.35f);
            }
        }
//        else
//        {
//            y -= (size * 0.025f);
//            size *= 0.95f;
//        }

        Texture background = Type.GetBackground(level);
        if (background != null)
        {
            RenderHelpers.DrawOnCardAuto(sb, card, background, new Vector2(x, y), size, size, color, 1f, 1f, 0);
        }

        RenderHelpers.DrawOnCardAuto(sb, card, Type.GetIcon(), new Vector2(x, y), size, size, Color.WHITE, 1f, 1f, 0f);

        Texture border = Type.GetBorder(level);
        if (border != null)
        {
            RenderHelpers.DrawOnCardAuto(sb, card, border, new Vector2(x, y), size, size, color, 1f, 1f, rotation);
        }

        Texture foreground = Type.GetForeground(level);
        if (foreground != null)
        {
            RenderHelpers.DrawOnCardAuto(sb, card, foreground, new Vector2(x, y), size, size, color, 1f, 1f, -rotation);
        }
    }

    @Override
    public int compareTo(EYBCardAffinity other)
    {
        return other.calculateRank() - calculateRank();
    }

    public int calculateRank()
    {
        if (Type == AffinityType.Star)
        {
            return 500 + level;
        }

        return (level * 100) + (10 - Type.ID);
    }

    public static void RenderBackground()
    {

    }

    public static void RenderForeground()
    {

    }
}