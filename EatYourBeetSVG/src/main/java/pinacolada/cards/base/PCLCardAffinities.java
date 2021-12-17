package pinacolada.cards.base;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import eatyourbeets.cards.base.EYBCardAffinities;
import eatyourbeets.cards.base.EYBCardAffinity;
import eatyourbeets.utilities.AdvancedTexture;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

import java.util.ArrayList;

public class PCLCardAffinities
{
    private static final AdvancedTexture upgradeCircle = new AdvancedTexture(GR.PCL.Images.Circle.Texture(), Settings.GREEN_RELIC_COLOR);

    public final ArrayList<PCLCardAffinity> List = new ArrayList<>();
    public PCLCard Card;
    public PCLCardAffinity Star = null;
    public boolean displayUpgrades = false;

    public PCLCardAffinities(PCLCard card)
    {
        Card = card;
    }

    public PCLCardAffinities(PCLCard card, EYBCardAffinities affinities)
    {
        Card = card;
        Initialize(affinities);
    }

    public PCLCardAffinities(PCLCard card, PCLCardAffinities affinities)
    {
        Card = card;
        Initialize(affinities);
    }

    public PCLCardAffinities Initialize(PCLAffinity affinity, int base, int upgrade, int scaling, int requirement)
    {
        if (base > 0 || upgrade > 0 || scaling > 0 || requirement > 0)
        {
            PCLCardAffinity a = Set(affinity, base);
            a.upgrade = upgrade;
            a.scaling = scaling;
            a.requirement = requirement;
        }
        Refresh();
        return this;
    }

    public PCLCardAffinities Initialize(EYBCardAffinities affinities)
    {
        if (affinities.Star != null)
        {
            Star = new PCLCardAffinity(PCLAffinity.Star, affinities.Star.level);
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
            PCLCardAffinity t = new PCLCardAffinity(PCLGameUtilities.ConvertEYBToPCLAffinity(a.type), a.level);
            t.scaling = a.scaling;
            t.upgrade = a.upgrade;
            t.requirement = a.requirement;
            List.add(t);
        }
        Refresh();
        return this;
    }

    public PCLCardAffinities Initialize(PCLCardAffinities affinities)
    {
        if (affinities.Star != null)
        {
            Star = new PCLCardAffinity(PCLAffinity.Star, affinities.Star.level);
            Star.scaling = affinities.Star.scaling;
            Star.upgrade = affinities.Star.upgrade;
            Star.requirement = affinities.Star.requirement;
        }
        else
        {
            Star = null;
        }

        List.clear();
        for (PCLCardAffinity a : affinities.List)
        {
            PCLCardAffinity t = new PCLCardAffinity(a.type, a.level);
            t.scaling = a.scaling;
            t.upgrade = a.upgrade;
            t.requirement = a.requirement;
            List.add(t);
        }
        Refresh();
        return this;
    }

    public void Clear()
    {
        List.clear();
        Star = null;
    }

    public void Refresh()
    {
        List.sort(PCLCardAffinity::compareTo);
    }

    public void ApplyUpgrades()
    {
        if (Star != null)
        {
            Star.level += Star.upgrade;
        }

        for (PCLCardAffinity a : List)
        {
            a.level += a.upgrade;
        }

        Refresh();
    }

    public void Add(int red, int green, int blue, int orange, int light, int dark, int silver)
    {
        Add(PCLAffinity.Red, red);
        Add(PCLAffinity.Green, green);
        Add(PCLAffinity.Blue, blue);
        Add(PCLAffinity.Orange, orange);
        Add(PCLAffinity.Light, light);
        Add(PCLAffinity.Dark, dark);
        Add(PCLAffinity.Silver, silver);
    }

    public void Set(int red, int green, int blue, int orange, int light, int dark, int silver)
    {
        Set(PCLAffinity.Red, red);
        Set(PCLAffinity.Green, green);
        Set(PCLAffinity.Blue, blue);
        Set(PCLAffinity.Orange, orange);
        Set(PCLAffinity.Light, light);
        Set(PCLAffinity.Dark, dark);
        Set(PCLAffinity.Silver, silver);
    }

    public PCLCardAffinity AddStar(int level)
    {
        return SetStar((Star == null ? 0 : Star.level) + level);
    }

    public PCLCardAffinity SetStar(int level)
    {
        if (Star != null)
        {
            Star.level = level;
        }
        else
        {
            Star = new PCLCardAffinity(PCLAffinity.Star, level);
        }

        return Star;
    }

    public boolean HasStar()
    {
        return Star != null && Star.level > 0;
    }

    public boolean IsEmpty() {
        return (List.isEmpty() || PCLJUtils.All(List, af -> af.level <= 0)) && !HasStar();
    }

    public PCLCardAffinities Add(PCLCardAffinities other, int levelLimit)
    {
        if (other != null)
        {
            int star = Math.min(levelLimit, other.GetLevel(PCLAffinity.Star));
            if (star > 0)
            {
                AddStar(star);
                //Add(star, star, star, star, star, star);
            }
            else for (PCLCardAffinity item : other.List)
            {
                Add(item.type, Math.min(levelLimit, item.level));
            }
        }

        return this;
    }

    public PCLCardAffinities Add(PCLCardAffinities other)
    {
        if (other.Star != null){
            AddStar(other.Star.level);
        }
        for (PCLCardAffinity item : other.List)
        {
            Add(item.type, item.level);
        }

        return this;
    }

    public PCLCardAffinity Add(PCLAffinity affinity, int level)
    {
        if (affinity == PCLAffinity.Star)
        {
            return AddStar(level);
        }

        for (PCLCardAffinity a : List)
        {
            if (a.type == affinity)
            {
                a.level = Math.max(0, a.level + level);
                Refresh();
                return a;
            }
        }

        PCLCardAffinity a = new PCLCardAffinity(affinity, level);
        List.add(a);
        Refresh();
        return a;
    }

    public PCLCardAffinity Set(PCLAffinity affinity, int level)
    {
        if (level < 0) {
            level = 0;
        }
        if (affinity == PCLAffinity.Star)
        {
            SetStar(level);
            return Star;
        }

        PCLCardAffinity result;
        for (PCLCardAffinity PCLCardAffinity : List)
        {
            result = PCLCardAffinity;
            if (result.type == affinity)
            {
                result.level = level;
                Refresh();
                return result;
            }
        }

        result = new PCLCardAffinity(affinity, level);
        List.add(result);
        Refresh();
        return result;
    }

    public PCLCardAffinity AddScaling(PCLAffinity affinity, int level) {
        PCLCardAffinity a = Get(affinity);
        a.scaling = Math.max(0,a.scaling + level);
        return a;
    }

    public PCLCardAffinity SetScaling(PCLAffinity affinity, int level) {
        PCLCardAffinity a = Get(affinity);
        a.scaling = Math.max(0,level);
        return a;
    }

    public PCLCardAffinity Get(PCLAffinity affinity)
    {
        return Get(affinity, false);
    }

    public PCLCardAffinity Get(PCLAffinity affinity, boolean createIfNull)
    {
        if (affinity == PCLAffinity.General)
        {
            final int star = Star != null ? Star.level : 0;
            final PCLCardAffinity a = List.size() > 0 ? List.get(0) : null;
            return a != null && a.level >= star ? a : Star;
        }

        if (affinity == PCLAffinity.Star)
        {
            return (createIfNull && Star == null) ? SetStar(0) : Star;
        }

        for (PCLCardAffinity item : List)
        {
            if (item.type == affinity)
            {
                return item;
            }
        }

        return createIfNull ? Set(affinity, 0) : null;
    }

    public ArrayList<PCLAffinity> GetAffinities() {
        final ArrayList<PCLAffinity> list = new ArrayList<>();
        for (PCLCardAffinity item : List)
        {
            if (item.level > 0)
            {
                list.add(item.type);
            }
        }
        return list;
    }

    public int GetScaling(PCLAffinity affinity, boolean useStarScaling)
    {
        final int star = (Star != null ? Star.scaling : 0);
        if (affinity == PCLAffinity.Star)
        {
            return star;
        }

        int scaling = 0;
        if (useStarScaling)
        {
            scaling = star;
        }

        final PCLCardAffinity a = Get(affinity);
        if (a != null)
        {
            scaling += a.scaling;
        }

        return scaling;
    }

    public int GetUpgrade(PCLAffinity type)
    {
        return GetUpgrade(type, true);
    }

    public int GetUpgrade(PCLAffinity affinity, boolean useStar)
    {
        final int star = (Star != null ? Star.upgrade : 0);
        if (affinity == PCLAffinity.Star || (useStar && star > 0))
        {
            return star;
        }
        else if (affinity == null || affinity == PCLAffinity.General) // Highest level among all affinities
        {
            return List.isEmpty() ? star : List.get(0).upgrade;
        }
        else
        {
            final PCLCardAffinity a = Get(affinity);
            return (a != null) ? a.upgrade : 0;
        }
    }

    public int GetLevel(PCLAffinity affinity)
    {
        return GetLevel(affinity, true);
    }

    public int GetLevel(PCLAffinity affinity, boolean useStarLevel)
    {
        int star = (Star != null ? Star.level : 0);
        if (affinity == PCLAffinity.Star || (useStarLevel && star > 0))
        {
            return star;
        }
        else if (affinity == PCLAffinity.General)
        {
            return List.isEmpty() ? (useStarLevel ? star : 0) : List.get(0).level; // Highest level among all affinities
        }
        else
        {
            final PCLCardAffinity a = Get(affinity);
            return (a != null) ? a.level : 0;
        }
    }

    public PCLCardAffinity GetDirectly(PCLAffinity affinity)
    {
        if (affinity == PCLAffinity.Star)
        {
            return Star;
        }

        for (PCLCardAffinity a : List)
        {
            if (a.type == affinity)
            {
                return a;
            }
        }

        return null;
    }

    public int GetDirectLevel(PCLAffinity affinity)
    {
        final PCLCardAffinity a = GetDirectly(affinity);
        return (a != null) ? a.level : 0;
    }

    public void SetRequirement(PCLAffinity affinity, int requirement)
    {
        Get(affinity == PCLAffinity.General ? PCLAffinity.Star : affinity, true).requirement = requirement;
    }

    public int GetRequirement(PCLAffinity affinity)
    {
        final PCLCardAffinity a = Get(affinity == PCLAffinity.General ? PCLAffinity.Star : affinity);
        return a == null ? 0 : a.requirement;
    }

    public void RenderOnCard(SpriteBatch sb, PCLCard card, boolean highlight)
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
        for (PCLCardAffinity PCLCardAffinity : List)
        {
            if (PCLCardAffinity.level <= 0)
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
            final PCLCardAffinity item = List.get(i);

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
        for (PCLCardAffinity t : List)
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
            final PCLCardAffinity item = List.get(i);
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