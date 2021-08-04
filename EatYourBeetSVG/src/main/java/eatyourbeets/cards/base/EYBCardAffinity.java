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
    public final AffinityType Type;
    public int level;
    public int scaling;
    public int upgrade;
    public int requirement;

    public EYBCardAffinity(AffinityType type)
    {
        this.Type = type;
    }

    public EYBCardAffinity(AffinityType type, int level)
    {
        this.Type = type;
        this.level = level;
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

    @Override
    public String toString()
    {
        return Type + ": " + level + " (+" + upgrade + "), s:" + scaling;
    }

    public void RenderOnCard(SpriteBatch sb, EYBCard card, float x, float y, float size, boolean highlight)
    {
        float rotation = 0f;
        float borderScale = 1f;
        final Color color = Color.WHITE.cpy();
        Color backgroundColor = color.cpy();
        Color borderColor = color;
        if (highlight)
        {
            borderColor = Settings.GREEN_RELIC_COLOR.cpy();
            borderColor.a = color.a;
            borderScale += GR.UI.Time_Sin(0.015f, 2.5f);
        }

        if (level > 1)
        {
            rotation = GR.UI.Time_Multi(-(card.isPopup ? 20 : 40));
            //borderColor.lerp(c.GetRarityColor(false), 0.35f);
        }

        Texture background = Type.GetBackground(level, upgrade);
        if (background != null)
        {
            RenderHelpers.DrawOnCardAuto(sb, card, background, new Vector2(x, y), size, size, Color.LIGHT_GRAY, 1f, 1f, 0);
        }

        RenderHelpers.DrawOnCardAuto(sb, card, Type.GetIcon(), new Vector2(x, y), size, size, color, 1f, 1f, 0f);

        Texture border = Type.GetBorder(level);
        if (border != null)
        {
            RenderHelpers.DrawOnCardAuto(sb, card, border, new Vector2(x, y), size, size, borderColor, 1f, borderScale, rotation);
        }

        Texture foreground = Type.GetForeground(level);
        if (foreground != null)
        {
            RenderHelpers.DrawOnCardAuto(sb, card, foreground, new Vector2(x, y), size, size, borderColor, 1f, borderScale, -rotation);
        }

        if (Type == AffinityType.Star)
        {
            Texture star = GR.Common.Images.Affinities.Star_FG.Texture();
            RenderHelpers.DrawOnCardAuto(sb, card, star, new Vector2(x, y), size, size, color, 1f, 1f, 0);
        }
    }

    public void Render(SpriteBatch sb, Color color, float cX, float cY, float size)
    {
        Texture background = Type.GetBackground(level, upgrade);
        if (background != null)
        {
            RenderHelpers.DrawCentered(sb, Color.LIGHT_GRAY, background, cX, cY, size, size, 1, 0);
        }

        RenderHelpers.DrawCentered(sb, color, Type.GetIcon(), cX, cY, size, size, 1, 0);

        Texture border = Type.GetBorder(level);
        if (border != null)
        {
            RenderHelpers.DrawCentered(sb, color, border, cX, cY, size, size, 1, 0);
        }

        Texture foreground = Type.GetForeground(level);
        if (foreground != null)
        {
            RenderHelpers.DrawCentered(sb, color, foreground, cX, cY, size, size, 1, 0);
        }

        if (Type == AffinityType.Star)
        {
            RenderHelpers.DrawCentered(sb, color, GR.Common.Images.Affinities.Star_FG.Texture(), cX, cY, size, size, 1, 0);
        }
    }
}