package eatyourbeets.cards.base;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.core.Settings;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.RenderHelpers;

public class EYBCardAffinity implements Comparable<EYBCardAffinity>
{
    public final Affinity type;

    public int level;
    public int scaling;
    public int upgrade;
    public int requirement;

    public EYBCardAffinity(Affinity affinity)
    {
        this.type = affinity;
    }

    public EYBCardAffinity(Affinity affinity, int level)
    {
        this.type = affinity;
        this.level = level;
    }

    @Override
    public int compareTo(EYBCardAffinity other)
    {
        return other.calculateRank() - calculateRank();
    }

    public int calculateRank()
    {
        return (type == Affinity.Sealed) ? (-500)
             : (type == Affinity.Star) ? (500 + level)
             : ((level * 1000) + (upgrade * 10) + (Affinity.MAX_ID - type.ID));
    }

    @Override
    public String toString()
    {
        return type + ": " + level + " (+" + upgrade + "), s:" + scaling;
    }

    public void RenderOnCard(SpriteBatch sb, EYBCard card, float x, float y, float size, boolean highlight)
    {
        float rotation = 0f;
        float borderScale = 1f;
        Color mainColor = Color.WHITE.cpy();
        Color borderColor = Color.WHITE.cpy();

        if (highlight)
        {
            borderColor = Settings.GREEN_RELIC_COLOR.cpy();
            borderScale += GR.UI.Time_Sin(0.015f, 2.5f);
        }

        if (level > 1)
        {
            rotation = GR.UI.Time_Multi(-(card.isPopup ? 20 : 40));
        }

        final Texture background = type.GetBackground(level, upgrade);
        if (background != null)
        {
            RenderHelpers.DrawOnCardAuto(sb, card, background, new Vector2(x, y), size, size, Color.LIGHT_GRAY, borderColor.a, 1f, 0);
        }

        RenderHelpers.DrawOnCardAuto(sb, card, type.GetIcon(), new Vector2(x, y), size, size, mainColor, mainColor.a, 1f, 0f);

        if (borderColor != null)
        {
            final Texture border = type.GetBorder(level);
            if (border != null)
            {
                RenderHelpers.DrawOnCardAuto(sb, card, border, new Vector2(x, y), size, size, borderColor, borderColor.a, borderScale, rotation);
            }

            final Texture foreground = type.GetForeground(level);
            if (foreground != null)
            {
                RenderHelpers.DrawOnCardAuto(sb, card, foreground, new Vector2(x, y), size, size, borderColor, borderColor.a, borderScale, -rotation);
            }
        }

        if (type == Affinity.Star)
        {
            final Texture star = GR.Common.Images.Affinities.Star_FG.Texture();
            RenderHelpers.DrawOnCardAuto(sb, card, star, new Vector2(x, y), size, size, mainColor, mainColor.a, 1f, 0);
        }
    }

    public void Render(SpriteBatch sb, Color color, float cX, float cY, float size)
    {
        final Texture background = type.GetBackground(level, upgrade);
        if (background != null)
        {
            RenderHelpers.DrawCentered(sb, Color.LIGHT_GRAY, background, cX, cY, size, size, 1, 0);
        }

        RenderHelpers.DrawCentered(sb, color, type.GetIcon(), cX, cY, size, size, 1, 0);

        final Texture border = type.GetBorder(level);
        if (border != null)
        {
            RenderHelpers.DrawCentered(sb, color, border, cX, cY, size, size, 1, 0);
        }

        final Texture foreground = type.GetForeground(level);
        if (foreground != null)
        {
            RenderHelpers.DrawCentered(sb, color, foreground, cX, cY, size, size, 1, 0);
        }

        if (type == Affinity.Star)
        {
            RenderHelpers.DrawCentered(sb, color, GR.Common.Images.Affinities.Star_FG.Texture(), cX, cY, size, size, 1, 0);
        }
    }
}