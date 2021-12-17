package pinacolada.cards.base;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.core.Settings;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLRenderHelpers;

public class PCLCardAffinity implements Comparable<PCLCardAffinity>
{
    public final PCLAffinity type;

    public int level;
    public int scaling;
    public int upgrade;
    public int requirement;

    public PCLCardAffinity(PCLAffinity affinity)
    {
        this.type = affinity;
    }

    public PCLCardAffinity(PCLAffinity affinity, int level)
    {
        this.type = affinity;
        this.level = level;
    }

    @Override
    public int compareTo(PCLCardAffinity other)
    {
        return other.calculateRank() - calculateRank();
    }

    public int calculateRank()
    {
        if (type == PCLAffinity.Star)
        {
            return 500 + level;
        }

        return (level * 1000) + (upgrade * 10) + (PCLAffinity.MAX_ID - type.ID);
    }

    @Override
    public String toString()
    {
        return type + ": " + level + " (+" + upgrade + "), s:" + scaling;
    }

    public void RenderOnCard(SpriteBatch sb, PCLCard card, float x, float y, float size, boolean highlight)
    {
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

        Texture background = type.GetBackground(level, upgrade);
        if (background != null)
        {
            pinacolada.utilities.PCLRenderHelpers.DrawOnCardAuto(sb, card, background, new Vector2(x, y), size, size, Color.LIGHT_GRAY, 1f, 1f, 0);
        }

        pinacolada.utilities.PCLRenderHelpers.DrawOnCardAuto(sb, card, type.GetIcon(), new Vector2(x, y), size, size, color, 1f, 1f, 0f);

        Texture border = type.GetBorder(level);
        if (border != null)
        {
            PCLRenderHelpers.DrawOnCardAuto(sb, card, border, new Vector2(x, y), size, size, borderColor, 1f, borderScale, 0f);
        }

        if (type == PCLAffinity.Star)
        {
            Texture star = GR.PCL.Images.Affinities.Star_FG.Texture();
            PCLRenderHelpers.DrawOnCardAuto(sb, card, star, new Vector2(x, y), size, size, color, 1f, 1f, 0);
        }
    }

    public void Render(SpriteBatch sb, Color color, float cX, float cY, float size)
    {
        Texture background = type.GetBackground(level, upgrade);
        if (background != null)
        {
            pinacolada.utilities.PCLRenderHelpers.DrawCentered(sb, Color.LIGHT_GRAY, background, cX, cY, size, size, 1, 0);
        }

        pinacolada.utilities.PCLRenderHelpers.DrawCentered(sb, color, type.GetIcon(), cX, cY, size, size, 1, 0);

        Texture border = type.GetBorder(level);
        if (border != null)
        {
            PCLRenderHelpers.DrawCentered(sb, color, border, cX, cY, size, size, 1, 0);
        }

        if (type == PCLAffinity.Star)
        {
            PCLRenderHelpers.DrawCentered(sb, color, GR.PCL.Images.Affinities.Star_FG.Texture(), cX, cY, size, size, 1, 0);
        }
    }
}