package eatyourbeets.cards.base;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.JUtils;
import eatyourbeets.utilities.RenderHelpers;

public class EYBCardAffinity implements Comparable<EYBCardAffinity>
{
    public final AffinityType Type;
    public int level;
    public int scaling;
    public int upgrade;

    public EYBCardAffinity(AffinityType type)
    {
        this.Type = type;
    }

    public EYBCardAffinity(AffinityType type, int level)
    {
        this.Type = type;
        this.level = level;
    }

    public void RenderOnCard(SpriteBatch sb, AbstractCard card, float x, float y, float size, boolean highlight)
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

        if (highlight)
        {
            final float alpha = color.a;
            final float distance = 1;// Testing.TryGetValue(0, 0.5f);
            final float speed = 4; //Testing.TryGetValue(1, 4f);
            color = Color.WHITE.cpy().lerp(Settings.GREEN_RELIC_COLOR, GR.UI.Time_Sin(distance, speed));
            color.a = alpha;
        }

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

        if (Type == AffinityType.Star)
        {
            Texture star = GR.Common.Images.Affinities.Star_FG.Texture();
            Color starColor = new Color(1, 1, 1, color.a);
            RenderHelpers.DrawOnCardAuto(sb, card, star, new Vector2(x, y), size, size, starColor, 1f, 1f, 0);
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

        return (level * 1000) + (upgrade * 10) + (AffinityType.MAX_ID - Type.ID);
    }
}