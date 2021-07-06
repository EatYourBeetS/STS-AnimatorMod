package eatyourbeets.cards.base;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;

import java.util.ArrayList;

public class EYBCardAffinities
{
    public final ArrayList<EYBCardAffinity> List = new ArrayList<>();
    public EYBCardAffinity Star = null;

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

    public void Add(int red, int green, int blue, int light, int dark)
    {
        Add(EYBCardAffinityType.Red, red);
        Add(EYBCardAffinityType.Green, green);
        Add(EYBCardAffinityType.Blue, blue);
        Add(EYBCardAffinityType.Light, light);
        Add(EYBCardAffinityType.Dark, dark);
    }

    public void Set(int red, int green, int blue, int light, int dark)
    {
        Set(EYBCardAffinityType.Red, red);
        Set(EYBCardAffinityType.Green, green);
        Set(EYBCardAffinityType.Blue, blue);
        Set(EYBCardAffinityType.Light, light);
        Set(EYBCardAffinityType.Dark, dark);
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
            Star = new EYBCardAffinity(EYBCardAffinityType.Star, level);
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

    public void Add(EYBCardAffinityType type, int level)
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

    public void Set(EYBCardAffinityType type, int level)
    {
        for (int i = 0; i < List.size(); i++)
        {
            EYBCardAffinity a = List.get(i);
            if (a.Type == type)
            {
                if ((a.level = level) <= 0)
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

        if (level > 0)
        {
            List.add(new EYBCardAffinity(type, level));
            List.sort(EYBCardAffinity::compareTo);
        }
    }

    public EYBCardAffinity Get(EYBCardAffinityType type)
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

    public int GetLevel(EYBCardAffinityType type)
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
        float y = AbstractCard.RAW_H;

        if (highlight)
        {
            size = 54;
            y *= 0.60f;
        }
        else
        {
            size = 43;
            y *= 0.5f;// -0.47f;
        }

        if (HasStar())
        {
            Star.RenderOnCard(sb, card, 0, y, size);
            return;
        }

        float step = (size / AbstractCard.RAW_W) * 1.02f;
        int half = List.size() / 2;
        for (int i = 0; i < List.size(); i++)
        {
            final EYBCardAffinity item = List.get(i);
            float x = AbstractCard.RAW_W;

// Render Left
//            x *= -0.25f + (step * i);
// Render Left

// Render Centered
            if (List.size() % 2 == 1)
            {
                x *= (step * (i - half));
            }
            else
            {
                x *= (step * 0.5f) + (step * (i - half));
            }
// Render Centered

            item.RenderOnCard(sb, card, x, y, size);
        }
    }

    public void Render(SpriteBatch sb, float x, float y, float size)
    {
        int half = List.size() / 2;
        float step = size * 0.95f;
        for (int i = 0; i < List.size(); i++)
        {
            final EYBCardAffinity item = List.get(i);
            float offsetX = 0;
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