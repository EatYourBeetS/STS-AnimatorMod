package eatyourbeets.cards.base;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.ColoredTexture;
import eatyourbeets.utilities.JUtils;
import eatyourbeets.utilities.RenderHelpers;

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

    public boolean CanSynergize(EYBCardAffinities other)
    {
        for (EYBCardAffinity a : List)
        {
            for (EYBCardAffinity b : other.List)
            {
                if (a.Type == b.Type && (a.level > 1 || b.level > 1))
                {
                    return true;
                }
            }
        }

        return false;
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

    public void SetStar(int level)
    {
        if (level == 0)
        {
            Star = null;
        }
        else if (Star != null)
        {
            Star.level = 2;
        }
        else
        {
            Star = new EYBCardAffinity(AffinityType.Star, 2);
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
        for (EYBCardAffinity item : other.List)
        {
            Add(item.Type, Math.min(levelLimit, item.level));
        }
    }

    public void Add(AffinityType type, int level)
    {
        if (level > 0)
        {
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
        for (EYBCardAffinity item : List)
        {
            if (item.Type == type)
            {
                return item.level;
            }
        }

        return 0;
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
            y *= 0.51f;// -0.47f;
            step = size * 1.2f;
        }

        if (HasStar())
        {
            Star.RenderOnCard(sb, card, 0, y, size);
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

            item.RenderOnCard(sb, card, x, y, size);

            if (displayUpgrades && Upgrades.containsKey(item.Type))
            {
                upgradeCircle.color.a = GR.UI.Time_Sin(0.4f,4f);
                RenderHelpers.DrawOnCardAuto(sb, card, upgradeCircle, x, y, size * 0.95f, size * 0.95f);
            }
        }
    }

    public void Render(SpriteBatch sb, float x, float y, float size)
    {
        int half = List.size() / 2;
        float step = size * 0.95f;
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