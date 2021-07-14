package eatyourbeets.cards.base;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import eatyourbeets.utilities.EYBFontHelper;
import eatyourbeets.utilities.RenderHelpers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;

public class EYBCardAffinityStatistics implements Iterable<EYBCardAffinityStatistics.Group>
{
    protected final ArrayList<EYBCardAffinities> list = new ArrayList<>();
    protected final ArrayList<Group> groups = new ArrayList<>();
    protected int cards;

    public EYBCardAffinityStatistics()
    {

    }

    public EYBCardAffinityStatistics(Collection<AbstractCard> cards)
    {
        AddCards(cards);
        RefreshStatistics();
    }

    public void AddCard(AbstractCard card)
    {
        EYBCardAffinities a = GetAffinities(card);
        if (a != null)
        {
            list.add(a);
        }

        cards += 1;
    }

    public void AddCards(CardGroup group)
    {
        AddCards(group.group);
    }

    public void AddCards(Collection<AbstractCard> cards)
    {
        for (AbstractCard c : cards)
        {
            AddCard(c);
        }
    }

    public void Reset()
    {
        cards = 0;
        list.clear();

        for (Group g : groups)
        {
            g.Reset();
        }
    }

    public ArrayList<Group> RefreshStatistics()
    {
        for (EYBCardAffinities a : list)
        {
            for (AffinityType t : AffinityType.AllTypes())
            {
                GetGroup(t).Add(a.GetLevel(t, true));
            }
        }

        groups.sort(Comparator.comparingInt(a -> -a.GetTotal()));// descending
        return groups;
    }

    public Group GetGroup(int index)
    {
        return groups.get(index);
    }

    public Group GetGroup(AffinityType type)
    {
        for (Group g : groups)
        {
            if (g.Type == type)
            {
                return g;
            }
        }

        Group g = new Group(type);
        groups.add(g);
        return g;
    }

    public static EYBCardAffinities GetAffinities(AbstractCard card)
    {
        return card instanceof EYBCard ? ((EYBCard) card).affinities : null;
    }

    @Override
    public Iterator<Group> iterator()
    {
        return groups.iterator();
    }

    public static class Group
    {
        public AffinityType Type;
        public int Size;
        public int Total_LV1;
        public int Total_LV2;

        public Group(AffinityType type)
        {
            Type = type;
        }

        public void Reset()
        {
            Size = Total_LV1 = Total_LV2 = 0;
        }

        public void Add(int level)
        {
            Size += 1;
            if (level == 1)
            {
                Total_LV1 += 1;
            }
            else if (level > 1)
            {
                Total_LV2 += 1;
            }
        }

        public int GetTotal()
        {
            return Total_LV1 + Total_LV2;
        }

        public int GetTotal(int level)
        {
            return level == 1 ? Total_LV1 : level > 1 ? Total_LV2 : GetTotal();
        }

        public float GetPercentage(int level)
        {
            return GetTotal(level) / (float)Size;
        }

        public String GetPercentageString(int level)
        {
            return Math.round(GetPercentage(level) * 100) + "%";
        }

        public void Render(SpriteBatch sb, float cX, float cY, float size, int level)
        {
            final BitmapFont font = EYBFontHelper.CardTitleFont_Large;
            font.getData().setScale(size* 0.00925f);

            RenderHelpers.DrawCentered(sb, Color.WHITE, Type.GetIcon(), cX, cY, size, size, 1, 0);
            if (level > 0)
            {
                Texture texture = Type.GetBorder(level);
                if (texture != null)
                {
                    RenderHelpers.DrawCentered(sb, Color.WHITE, texture, cX, cY, size, size, 1, 0);
                }

                texture = Type.GetForeground(level);
                if (texture != null)
                {
                    RenderHelpers.DrawCentered(sb, Color.WHITE, texture, cX, cY, size, size, 1, 0);
                }
            }
            RenderHelpers.WriteCentered(sb, font, GetPercentageString(0), cX + (size * 0.1f), cY - (size * 0.25f), Color.WHITE);

            RenderHelpers.ResetFont(font);
        }
    }
}