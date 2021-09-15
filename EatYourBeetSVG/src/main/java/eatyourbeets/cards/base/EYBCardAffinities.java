package eatyourbeets.cards.base;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.AdvancedTexture;

import java.util.ArrayList;

public class EYBCardAffinities
{
    private static final AdvancedTexture upgradeCircle = new AdvancedTexture(GR.Common.Images.Circle.Texture(), Settings.GREEN_RELIC_COLOR);

    public final ArrayList<EYBCardAffinity> List = new ArrayList<>();
    public EYBCard Card;
    public EYBCardAffinity Star = null;
    public boolean displayUpgrades = false;

    public EYBCardAffinities(EYBCard card)
    {
        Card = card;
    }

    public void Initialize(Affinity affinity, int base, int upgrade, int scaling, int requirement)
    {
        if (base > 0 || upgrade > 0 || scaling > 0 || requirement > 0)
        {
            EYBCardAffinity a = Set(affinity, base);
            a.upgrade = upgrade;
            a.scaling = scaling;
            a.requirement = requirement;
        }
        Refresh();
    }

    public void Initialize(EYBCardAffinities affinities)
    {
        if (affinities.Star != null)
        {
            Star = new EYBCardAffinity(Affinity.Star, affinities.Star.level);
            Star.scaling = affinities.Star.scaling;
            Star.upgrade = affinities.Star.upgrade;
            Star.requirement = affinities.Star.requirement;
        }
        else
        {
            Star = null;
        }

        List.clear();
        for (EYBCardAffinity a : affinities.List)
        {
            EYBCardAffinity t = new EYBCardAffinity(a.type, a.level);
            t.scaling = a.scaling;
            t.upgrade = a.upgrade;
            t.requirement = a.requirement;
            List.add(t);
        }
        Refresh();
    }

    public void Clear()
    {
        List.clear();
        Star = null;
    }

    public void Refresh()
    {
        List.sort(EYBCardAffinity::compareTo);
    }

    public void ApplyUpgrades()
    {
        if (Star != null)
        {
            Star.level += Star.upgrade;
        }

        for (EYBCardAffinity a : List)
        {
            a.level += a.upgrade;
        }

        Refresh();
    }

    public void Add(int red, int green, int blue, int orange, int light, int dark)
    {
        Add(Affinity.Red, red);
        Add(Affinity.Green, green);
        Add(Affinity.Blue, blue);
        Add(Affinity.Orange, orange);
        Add(Affinity.Light, light);
        Add(Affinity.Dark, dark);
    }

    public void Set(int red, int green, int blue, int orange, int light, int dark)
    {
        Set(Affinity.Red, red);
        Set(Affinity.Green, green);
        Set(Affinity.Blue, blue);
        Set(Affinity.Orange, orange);
        Set(Affinity.Light, light);
        Set(Affinity.Dark, dark);
    }

    public EYBCardAffinity AddStar(int level)
    {
        return SetStar((Star == null ? 0 : Star.level) + level);
    }

    public EYBCardAffinity SetStar(int level)
    {
        if (Star != null)
        {
            Star.level = level;
        }
        else
        {
            Star = new EYBCardAffinity(Affinity.Star, level);
        }

        return Star;
    }

    public boolean HasStar()
    {
        return Star != null && Star.level > 0;
    }

    public EYBCardAffinities Add(EYBCardAffinities other, int levelLimit)
    {
        if (other != null)
        {
            int star = Math.min(levelLimit, other.GetLevel(Affinity.Star));
            if (star > 0)
            {
                AddStar(star);
                Add(star, star, star, star, star, star);
            }
            else for (EYBCardAffinity item : other.List)
            {
                Add(item.type, Math.min(levelLimit, item.level));
            }
        }

        return this;
    }

    public EYBCardAffinities Add(EYBCardAffinities other)
    {
        for (EYBCardAffinity item : other.List)
        {
            Add(item.type, item.level);
        }

        return this;
    }

    public EYBCardAffinity Add(Affinity affinity, int level)
    {
        if (affinity == Affinity.Star)
        {
            return AddStar(level);
        }

        for (EYBCardAffinity a : List)
        {
            if (a.type == affinity)
            {
                a.level += level;
                Refresh();
                return a;
            }
        }

        EYBCardAffinity a = new EYBCardAffinity(affinity, level);
        List.add(a);
        Refresh();
        return a;
    }

    public EYBCardAffinity Set(Affinity affinity, int level)
    {
        if (affinity == Affinity.Star)
        {
            SetStar(level);
            return Star;
        }

        EYBCardAffinity result;
        for (EYBCardAffinity eybCardAffinity : List)
        {
            result = eybCardAffinity;
            if (result.type == affinity)
            {
                result.level = level;
                Refresh();
                return result;
            }
        }

        result = new EYBCardAffinity(affinity, level);
        List.add(result);
        Refresh();
        return result;
    }

    public EYBCardAffinity Get(Affinity affinity)
    {
        return Get(affinity, false);
    }

    public EYBCardAffinity Get(Affinity affinity, boolean createIfNull)
    {
        if (affinity == Affinity.General)
        {
            final int star = Star != null ? Star.level : 0;
            final EYBCardAffinity a = List.size() > 0 ? List.get(0) : null;
            return a != null && a.level >= star ? a : Star;
        }

        if (affinity == Affinity.Star)
        {
            return (createIfNull && Star == null) ? SetStar(0) : Star;
        }

        for (EYBCardAffinity item : List)
        {
            if (item.type == affinity)
            {
                return item;
            }
        }

        return createIfNull ? Set(affinity, 0) : null;
    }

    public int GetScaling(Affinity affinity, boolean useStarScaling)
    {
        final int star = (Star != null ? Star.scaling : 0);
        if (affinity == Affinity.Star)
        {
            return star;
        }

        int scaling = 0;
        if (useStarScaling)
        {
            scaling = star;
        }

        final EYBCardAffinity a = Get(affinity);
        if (a != null)
        {
            scaling += a.scaling;
        }

        return scaling;
    }

    public int GetUpgrade(Affinity type)
    {
        return GetUpgrade(type, true);
    }

    public int GetUpgrade(Affinity affinity, boolean useStar)
    {
        final int star = (Star != null ? Star.upgrade : 0);
        if (affinity == Affinity.Star || (useStar && star > 0))
        {
            return star;
        }
        else if (affinity == null || affinity == Affinity.General) // Highest level among all affinities
        {
            return List.isEmpty() ? star : List.get(0).upgrade;
        }
        else
        {
            final EYBCardAffinity a = Get(affinity);
            return (a != null) ? a.upgrade : 0;
        }
    }

    public int GetLevel(Affinity affinity)
    {
        return GetLevel(affinity, true);
    }

    public int GetLevel(Affinity affinity, boolean useStarLevel)
    {
        int star = (Star != null ? Star.level : 0);
        if (affinity == Affinity.Star || (useStarLevel && star > 0))
        {
            return star;
        }
        else if (affinity == Affinity.General)
        {
            return List.isEmpty() ? (useStarLevel ? star : 0) : List.get(0).level; // Highest level among all affinities
        }
        else
        {
            final EYBCardAffinity a = Get(affinity);
            return (a != null) ? a.level : 0;
        }
    }

    public EYBCardAffinity GetDirectly(Affinity affinity)
    {
        if (affinity == Affinity.Star)
        {
            return Star;
        }

        for (EYBCardAffinity a : List)
        {
            if (a.type == affinity)
            {
                return a;
            }
        }

        return null;
    }

    public int GetDirectLevel(Affinity affinity)
    {
        final EYBCardAffinity a = GetDirectly(affinity);
        return (a != null) ? a.level : 0;
    }

    public void SetRequirement(Affinity affinity, int requirement)
    {
        Get(affinity == Affinity.General ? Affinity.Star : affinity, true).requirement = requirement;
    }

    public int GetRequirement(Affinity affinity)
    {
        final EYBCardAffinity a = Get(affinity == Affinity.General ? Affinity.Star : affinity);
        return a == null ? 0 : a.requirement;
    }

    public void RenderOnCard(SpriteBatch sb, EYBCard card, boolean highlight)
    {
        float size;
        float step;
        float x;
        float y = AbstractCard.RAW_H;

        if (highlight)
        {
            size = 64;
            y *= 0.58f;
            step = size * 0.9f;
        }
        else
        {
            size = 48;//48;
            y *= 0.49f;// -0.51f;
            step = size * 1.2f;
        }

        if (HasStar())
        {
            Star.RenderOnCard(sb, card, 0, y, size, displayUpgrades && Star.upgrade > 0);
            return;
        }

        int max = 0;
        for (EYBCardAffinity eybCardAffinity : List)
        {
            if (eybCardAffinity.level <= 0)
            {
                break;
            }

            max += 1;
        }

        final int half = max / 2;
        if (half >= 2)
        {
            step *= 0.75f;
        }

        for (int i = 0; i < max; i++)
        {
            final EYBCardAffinity item = List.get(i);

// Render Left
//            x *= -0.25f + (step * i);
// Render Left

// Render Centered
            if (max % 2 == 1)
            {
                x = (step * (i - half));
            }
            else
            {
                x = (step * 0.5f) + (step * (i - half));
            }
// Render Centered

            item.RenderOnCard(sb, card, x, y, size, displayUpgrades && item.upgrade > 0);
        }
    }

    public void Render(SpriteBatch sb, float x, float y, float size)
    {
        int max = 0;
        for (EYBCardAffinity t : List)
        {
            if (t.level <= 0)
            {
                break;
            }

            max += 1;
        }

        final Color color = Color.WHITE.cpy();
        final float step = size * 0.9f;
        final int half = max / 2;
        for (int i = 0; i < max; i++)
        {
            float offsetX;
            final EYBCardAffinity item = List.get(i);
            if (max % 2 == 1)
            {
                offsetX = (step * (i - half));
            }
            else
            {
                offsetX = (step * 0.5f) + (step* (i - half));
            }

            item.Render(sb, color, x + offsetX, y, size);
        }
    }
}