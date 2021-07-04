package eatyourbeets.cards.base;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;

import java.util.ArrayList;

public class EYBCardAlignments
{
    public final ArrayList<EYBCardAlignment> List = new ArrayList<>();
    public EYBCardAlignment Star = null;

    public EYBCardAlignments()
    {

    }

    public boolean CanSynergize(EYBCardAlignments other)
    {
        for (EYBCardAlignment a : List)
        {
            for (EYBCardAlignment b : other.List)
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
        Add(EYBCardAlignmentType.Red, red);
        Add(EYBCardAlignmentType.Green, green);
        Add(EYBCardAlignmentType.Blue, blue);
        Add(EYBCardAlignmentType.Light, light);
        Add(EYBCardAlignmentType.Dark, dark);
    }

    public void Set(int red, int green, int blue, int light, int dark)
    {
        Set(EYBCardAlignmentType.Red, red);
        Set(EYBCardAlignmentType.Green, green);
        Set(EYBCardAlignmentType.Blue, blue);
        Set(EYBCardAlignmentType.Light, light);
        Set(EYBCardAlignmentType.Dark, dark);
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
            Star = new EYBCardAlignment(EYBCardAlignmentType.Star, level);
        }
    }

    public boolean HasStar()
    {
        return Star != null;
    }

    public void Add(EYBCardAlignments other)
    {
        for (EYBCardAlignment item : other.List)
        {
            Add(item.Type, item.level);
        }
    }

    public void Add(EYBCardAlignments other, int levelLimit)
    {
        for (EYBCardAlignment item : other.List)
        {
            Add(item.Type, Math.min(levelLimit, item.level));
        }
    }

    public void Add(EYBCardAlignmentType type, int level)
    {
        if (level > 0)
        {
            for (int i = 0; i < List.size(); i++)
            {
                EYBCardAlignment a = List.get(i);
                if (a.Type == type)
                {
                    if ((a.level += level) <= 0)
                    {
                        List.remove(a);
                    }
                    else
                    {
                        List.sort(EYBCardAlignment::compareTo);
                    }

                    return;
                }
            }

            List.add(new EYBCardAlignment(type, level));
            List.sort(EYBCardAlignment::compareTo);
        }
    }

    public void Set(EYBCardAlignmentType type, int level)
    {
        for (int i = 0; i < List.size(); i++)
        {
            EYBCardAlignment a = List.get(i);
            if (a.Type == type)
            {
                if ((a.level = level) <= 0)
                {
                    List.remove(a);
                }
                else
                {
                    List.sort(EYBCardAlignment::compareTo);
                }

                return;
            }
        }

        if (level > 0)
        {
            List.add(new EYBCardAlignment(type, level));
            List.sort(EYBCardAlignment::compareTo);
        }
    }

    public EYBCardAlignment Get(EYBCardAlignmentType type)
    {
        for (EYBCardAlignment item : List)
        {
            if (item.Type == type)
            {
                return item;
            }
        }

        return null;
    }

    public int GetLevel(EYBCardAlignmentType type)
    {
        for (EYBCardAlignment item : List)
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
            size = 41;
            y *= 0.49f;// -0.47f;
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
            final EYBCardAlignment item = List.get(i);
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
        float step = size * 0.995f;
        for (int i = 0; i < List.size(); i++)
        {
            final EYBCardAlignment item = List.get(i);
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