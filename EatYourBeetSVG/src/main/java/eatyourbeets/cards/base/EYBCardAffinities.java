package eatyourbeets.cards.base;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.ColoredTexture;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JUtils;

import java.util.ArrayList;
import java.util.HashMap;

public class EYBCardAffinities
{
    private static final ColoredTexture upgradeCircle = new ColoredTexture(GR.Common.Images.Circle.Texture(), Settings.GREEN_RELIC_COLOR);

    public final ArrayList<EYBCardAffinity> List = new ArrayList<>();
    public final HashMap<AffinityType, Integer> Upgrades = new HashMap<>();
    public EYBCardAffinity Star = null;
    public boolean displayUpgrades = false;

    public EYBCardAffinities()
    {

    }

    public void Initialize(int red, int green, int blue, int light, int dark)
    {
        Initialize(AffinityType.Red, red, 0);
        Initialize(AffinityType.Green, green, 0);
        Initialize(AffinityType.Blue, blue, 0);
        Initialize(AffinityType.Light, light, 0);
        Initialize(AffinityType.Dark, dark, 0);
    }

    public void Initialize(AffinityType type, int base, int upgrade)
    {
        if (base > 0)
        {
            Set(type, base);
        }

        if (upgrade > 0)
        {
            JUtils.IncrementMapElement(Upgrades, type, upgrade);
        }
    }

    public void ApplyUpgrades()
    {
        for (AffinityType t : Upgrades.keySet())
        {
            Add(t, Upgrades.get(t));
        }
    }

    public void Add(int red, int green, int blue, int light, int dark)
    {
        Add(AffinityType.Red, red);
        Add(AffinityType.Green, green);
        Add(AffinityType.Blue, blue);
        Add(AffinityType.Light, light);
        Add(AffinityType.Dark, dark);
    }

    public void Set(int red, int green, int blue, int light, int dark)
    {
        Set(AffinityType.Red, red);
        Set(AffinityType.Green, green);
        Set(AffinityType.Blue, blue);
        Set(AffinityType.Light, light);
        Set(AffinityType.Dark, dark);
    }

    public void AddStar(int level)
    {
        SetStar((Star == null ? 0 : Star.level) + level);
    }

    public void SetStar(int level)
    {
        if (level == 0)
        {
            Star = null;
        }
        else if (Star != null)
        {
            Star.level = level;
        }
        else
        {
            Star = new EYBCardAffinity(AffinityType.Star, level);
        }
    }

    public boolean HasStar()
    {
        return Star != null;
    }

    public void Add(EYBCardAffinities other)
    {
        for (EYBCardAffinity item : other.List)
        {
            Add(item.Type, item.level);
        }
    }

    public void Add(EYBCardAffinities other, int levelLimit)
    {
        int star = Math.min(levelLimit, other.GetLevel(AffinityType.Star));
        if (star > 0)
        {
            AddStar(star);
            Add(star, star, star, star, star);
        }
        else for (EYBCardAffinity item : other.List)
        {
            Add(item.Type, Math.min(levelLimit, item.level));
        }
    }

    public void Add(AffinityType type, int level)
    {
        if (level > 0)
        {
            if (type == AffinityType.Star)
            {
                AddStar(level);
                return;
            }

            for (int i = 0; i < List.size(); i++)
            {
                EYBCardAffinity a = List.get(i);
                if (a.Type == type)
                {
                    if ((a.level += level) <= 0)
                    {
                        List.remove(a);
                    }
                    else
                    {
                        List.sort(EYBCardAffinity::compareTo);
                    }

                    return;
                }
            }

            List.add(new EYBCardAffinity(type, level));
            List.sort(EYBCardAffinity::compareTo);
        }
    }

    public EYBCardAffinity Set(AffinityType type, int level)
    {
        if (type == AffinityType.Star)
        {
            SetStar(level);
            return Star;
        }

        EYBCardAffinity result = null;
        for (int i = 0; i < List.size(); i++)
        {
            result = List.get(i);
            if (result.Type == type)
            {
                if ((result.level = level) <= 0)
                {
                    List.remove(result);
                }
                else
                {
                    List.sort(EYBCardAffinity::compareTo);
                }

                return result;
            }
        }

        if (level > 0)
        {
            result = new EYBCardAffinity(type, level);
            List.add(result);
            List.sort(EYBCardAffinity::compareTo);
        }

        return result;
    }

    public EYBCardAffinity Get(AffinityType type)
    {
        if (type == AffinityType.Star)
        {
            return Star;
        }

        for (EYBCardAffinity item : List)
        {
            if (item.Type == type)
            {
                return item;
            }
        }

        return null;
    }

    public int GetLevel(AffinityType type)
    {
        return GetLevel(type, true);
    }

    public int GetLevel(AffinityType type, boolean useStarLevel)
    {
        int star = (Star != null ? Star.level : 0);
        if (type == AffinityType.Star || (useStarLevel && star > 0))
        {
            return star;
        }
        else if (type == null) // Highest level among all affinities
        {
            return List.isEmpty() ? star : List.get(0).level;
        }
        else
        {
            EYBCardAffinity affinity = Get(type);
            return (affinity != null) ? affinity.level : 0;
        }
    }

    public boolean CanSynergize(EYBCardAffinities other)
    {
        return GetSynergies(other).GetLevel(null) > 0;
    }

    public EYBCardAffinities GetSynergies(EYBCardAffinities other)
    {
        final EYBCardAffinities synergies = new EYBCardAffinities();
        final int star = GetLevel(AffinityType.Star);
        if (star > 0)
        {
            int lv_b = other.GetLevel(null);
            if (lv_b > 0)
            {
                synergies.Add(AffinityType.Star, star);
            }

            return synergies;
        }

        for (AffinityType type : AffinityType.BasicTypes())
        {
            int lv_a = GetLevel(type);
            int lv_b = other.GetLevel(type);
            if ((lv_a > 1 && lv_b > 0) || (lv_b > 1 && lv_a > 0))
            {
                synergies.Add(type, lv_a);
            }
        }

        return synergies;
    }

    public void OnSynergy(AnimatorCard card)
    {
        EYBCard c1 = JUtils.SafeCast(card, EYBCard.class);
        if (c1 == null)
        {
            JUtils.LogError(this, "OnSynergy received null card reference.");
            return;
        }

        if (c1.affinities.HasStar())
        {
            if (CombatStats.TryActivateSemiLimited(AffinityType.Star.name()))
            {
                GameActions.Bottom.SynergyEffect(AffinityType.Star);
            }
        }

        for (EYBCardAffinity affinity : c1.affinities.List)
        {
            if (affinity.level >= 2 && CombatStats.TryActivateSemiLimited(affinity.Type.name()))
            {
                GameActions.Bottom.SynergyEffect(affinity.Type);
            }
        }
    }

    public void RenderOnCard(SpriteBatch sb, EYBCard card, boolean highlight)
    {
        float size;
        float step;
        float x;
        float y = AbstractCard.RAW_H;
        int half = List.size() / 2;

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
            Star.RenderOnCard(sb, card, 0, y, size, displayUpgrades && Upgrades.containsKey(Star.Type));
            return;
        }

        if (half >= 2)
        {
            step *= 0.75f;
        }

        for (int i = 0; i < List.size(); i++)
        {
            final EYBCardAffinity item = List.get(i);

// Render Left
//            x *= -0.25f + (step * i);
// Render Left

// Render Centered
            if (List.size() % 2 == 1)
            {
                x = (step * (i - half));
            }
            else
            {
                x = (step * 0.5f) + (step * (i - half));
            }
// Render Centered

            item.RenderOnCard(sb, card, x, y, size, displayUpgrades && Upgrades.containsKey(item.Type));
        }
    }

    public void Render(SpriteBatch sb, float x, float y, float size)
    {
        int half = List.size() / 2;
        float step = size * 0.9f;
        for (int i = 0; i < List.size(); i++)
        {
            float offsetX;
            final EYBCardAffinity item = List.get(i);
            if (List.size() % 2 == 1)
            {
                offsetX = (step * (i - half));
            }
            else
            {
                offsetX = (step * 0.5f) + (step* (i - half));
            }

            item.Render(sb, x + offsetX, y, size);
        }
    }
}