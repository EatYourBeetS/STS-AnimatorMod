package eatyourbeets.rewards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.random.Random;
import eatyourbeets.cards.base.AnimatorCard_UltraRare;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.relics.animator.Destiny;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.animator.loadouts._FakeLoadout;
import eatyourbeets.resources.animator.misc.AnimatorLoadout;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;
import eatyourbeets.utilities.Mathf;
import eatyourbeets.utilities.WeightedList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnimatorUltrarareGenerator
{
    public static final float BASE_CHANCE = 1.5f;
    public static final AnimatorLoadout COLORLESS_LOADOUT = new _FakeLoadout();

    protected final Random rng;
    protected final ArrayList<AbstractCard> cards;
    protected final ArrayList<AnimatorLoadout> series;
    protected float chance;

    public static float GetBaseChance()
    {
        final Float rate = GR.Common.Dungeon.GetFloat("UR_RATE", null);
        if (rate != null)
        {
            if (rate > 0 && GameUtilities.InGame())
            {
                GR.Common.Dungeon.SetCheating();
            }

            return rate;
        }

        float bonus = 1;
        int level = GR.Animator.Data.SpecialTrophies.Trophy1;
        if (level > 0)
        {
            bonus += level / (level + 100f);
        }

        if (GameUtilities.HasRelic(Destiny.ID))
        {
            bonus *= 1.5f;
        }

        return bonus * BASE_CHANCE;
    }

    public static boolean TryAdd(ArrayList<AbstractCard> cards, CardSeries specificSeries)
    {
        final int currentLevel = GR.Animator.GetUnlockLevel();
        if (currentLevel <= 2 || cards.isEmpty() || AbstractDungeon.floorNum < 8 || AbstractDungeon.floorNum > 36)
        {
            return false;
        }

        final AnimatorUltrarareGenerator gen = new AnimatorUltrarareGenerator(cards);
        if (specificSeries != null)
        {
            final AnimatorLoadout loadout = GR.Animator.Data.GetLoadout(specificSeries);
            gen.series.add(loadout != null ? loadout : COLORLESS_LOADOUT);
        }
        else
        {
            final ArrayList<CardSeries> deckSeries = new ArrayList<>();
            final ArrayList<AnimatorLoadout> toIgnore = new ArrayList<>();
            for (AbstractCard c : AbstractDungeon.player.masterDeck.group)
            {
                if (c instanceof AnimatorCard_UltraRare)
                {
                    final AnimatorLoadout loadout = GR.Animator.Data.GetLoadout(GameUtilities.GetSeries(c));
                    toIgnore.add(loadout != null ? loadout : COLORLESS_LOADOUT);
                }
                else
                {
                    final CardSeries s = GameUtilities.GetSeries(c);
                    if (s != null)
                    {
                        deckSeries.add(s);
                    }
                }
            }

            if (toIgnore.size() > 0)
            {
                gen.chance /= (toIgnore.size() * 2f);
            }

            final List<AnimatorLoadout> loadouts = GR.Animator.Data.GetEveryLoadout();
            final HashMap<AnimatorLoadout, Integer> map = new HashMap<>();
            for (AnimatorLoadout loadout : loadouts)
            {
                for (int i = deckSeries.size() - 1; i >= 0; i--)
                {
                    if (deckSeries.get(i) == loadout.Series)
                    {
                        if (!toIgnore.contains(loadout))
                        {
                            JUtils.IncrementMapElement(map, loadout, 1);
                        }

                        deckSeries.remove(i);
                    }
                }
            }

            if (!toIgnore.contains(COLORLESS_LOADOUT) && deckSeries.size() > 0)
            {
                map.put(COLORLESS_LOADOUT, deckSeries.size());
            }

            final WeightedList<AnimatorLoadout> weighted = new WeightedList<>();
            for (Map.Entry<AnimatorLoadout, Integer> entry : map.entrySet())
            {
                final AnimatorLoadout item = entry.getKey();
                final int weight = entry.getValue();
                if (GameUtilities.IsTestMode())
                {
                    JUtils.LogInfo(gen, "Adding UltraRare chance: [" + (item == COLORLESS_LOADOUT ? "Colorless" : item.Name) + ", " + weight + "]");
                }

                weighted.Add(item, weight);
            }

            while (weighted.Size() > 0)
            {
                gen.series.add(weighted.Retrieve(gen.rng, true));
            }
        }

        return gen.TryAddUltrarare();
    }

    public AnimatorUltrarareGenerator(ArrayList<AbstractCard> cards)
    {
        this.cards = cards;
        this.series = new ArrayList<>();
        this.rng = new Random(AbstractDungeon.cardRng.randomLong());
        this.chance = GetBaseChance();
    }

    protected boolean TryAddUltrarare()
    {
        if (series.isEmpty())
        {
            return false;
        }

        final ArrayList<AbstractCard> toAdd = new ArrayList<>();
        final int max = Mathf.Min(series.size(), cards.size());
        float realChance = chance;
        int i = 0;
        while (i < max)
        {
            float roll = rng.random(100f);
            if (GameUtilities.IsTestMode())
            {
                JUtils.LogInfo(this, "Rolling for ur: " + roll + " < " + realChance + " ? " + (roll < realChance));
            }

            if (roll < realChance)
            {
                toAdd.add(series.get(i++).GetUltraRare().CreateNewInstance());
                realChance = (100 / (i + 1f));
            }
            else
            {
                break;
            }
        }

        if (toAdd.isEmpty())
        {
            return false;
        }

        if (toAdd.size() == 1)
        {
            cards.set(Math.min(1, cards.size() - 1), toAdd.get(0));
        }
        else
        {
            for (int k = 0; k < toAdd.size(); k++)
            {
                cards.set(k, toAdd.get(k));
            }
        }

        return true;
    }
}
