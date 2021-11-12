package eatyourbeets.ui.animator.seriesSelection;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.random.Random;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.animator.misc.AnimatorLoadout;
import eatyourbeets.resources.animator.misc.AnimatorRuntimeLoadout;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;
import eatyourbeets.utilities.RandomizedList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class AnimatorLoadoutsContainer
{
    public static final int PROMOTED_COUNT = 3;
    public int TotalCardsInPool = 0;
    public final Map<AbstractCard, AnimatorRuntimeLoadout> cardsMap = new HashMap<>();
    public final ArrayList<AbstractCard> promotedCards = new ArrayList<>();
    public final ArrayList<AbstractCard> selectedCards = new ArrayList<>();
    public final ArrayList<AbstractCard> betaCards = new ArrayList<>();
    public final ArrayList<AbstractCard> allCards = new ArrayList<>();
    public int selectSeriesCache = 0;
    public int expandedSeriesCache = 0;

    public static void PreloadResources()
    {
        CardCrawlGame.sound.preload("CARD_SELECT");
        for (AnimatorLoadout loadout : GR.Animator.Data.BaseLoadouts)
        {
            AnimatorRuntimeLoadout temp = AnimatorRuntimeLoadout.TryCreate(loadout);
            if (temp != null)
            {
                temp.BuildCard();
            }
        }
    }

    public void CreateCards()
    {
        TotalCardsInPool = 0;

        cardsMap.clear();
        selectedCards.clear();
        promotedCards.clear();
        betaCards.clear();
        allCards.clear();

        selectSeriesCache = 0;
        expandedSeriesCache = 0;
        int promotedCount = 0;
        final RandomizedList<AnimatorRuntimeLoadout> toPromote = new RandomizedList<>();
        final ArrayList<AnimatorRuntimeLoadout> seriesSelectionItems = new ArrayList<>();
        for (AnimatorLoadout loadout : GR.Animator.Data.BaseLoadouts)
        {
            final AnimatorRuntimeLoadout card = AnimatorRuntimeLoadout.TryCreate(loadout);
            if (card != null)
            {
                if (loadout == GR.Animator.Data.SelectedLoadout)
                {
                    card.Promote();
                    promotedCount += 1;
                }
                else
                {
                    toPromote.Add(card);
                }

                seriesSelectionItems.add(card);
            }
        }

        for (AnimatorLoadout loadout : GR.Animator.Data.BetaLoadouts)
        {
            final AnimatorRuntimeLoadout card = AnimatorRuntimeLoadout.TryCreate(loadout);
            if (card != null)
            {
                if (loadout == GR.Animator.Data.SelectedLoadout)
                {
                    card.Promote();
                    promotedCount += 1;
                }

                seriesSelectionItems.add(card);
            }
        }

        final Random rng = new Random(Settings.seed + 13);
        while (promotedCount < PROMOTED_COUNT)
        {
            toPromote.Retrieve(rng).Promote();
            promotedCount += 1;
        }

        for (AnimatorRuntimeLoadout c : seriesSelectionItems)
        {
            final AbstractCard card = c.BuildCard();
            if (card != null)
            {
                cardsMap.put(c.BuildCard(), c);
            }
            else
            {
                JUtils.LogError(this, "AnimatorRuntimeLoadout.BuildCard() failed, " + c.Loadout.Name);
            }
        }
    }

    public AnimatorRuntimeLoadout Find(AbstractCard card)
    {
        return cardsMap.get(card);
    }

    public Collection<AnimatorRuntimeLoadout> GetAllCards()
    {
        return cardsMap.values();
    }

    public boolean ToggleExpansion(AbstractCard card)
    {
        AnimatorRuntimeLoadout c = Find(card);
        return ToggleExpansion(c, !c.expansionEnabled);
    }

    public boolean ToggleExpansion(AbstractCard card, boolean value)
    {
        AnimatorRuntimeLoadout c = Find(card);
        return ToggleExpansion(c, value);
    }

    public boolean ToggleExpansion(AnimatorRuntimeLoadout c, boolean value)
    {
        if (c.canEnableExpansion) {
            TotalCardsInPool -= c.GetCardPoolInPlay().size();
            c.ToggleExpansion(value);
            TotalCardsInPool += c.GetCardPoolInPlay().size();
            expandedSeriesCache += c.expansionEnabled ? 1 : -1;
            return true;
        }
        return false;
    }


    public boolean Select(AbstractCard card)
    {
        if (!selectedCards.contains(card))
        {
            TotalCardsInPool += Find(card).GetCardPoolInPlay().size();
            selectedCards.add(card);
            selectSeriesCache += 1;
            return true;
        }

        return false;
    }

    public boolean Deselect(AbstractCard card)
    {
        AnimatorRuntimeLoadout c = Find(card);
        if (!c.promoted && selectedCards.remove(card))
        {
            TotalCardsInPool -= c.GetCardPoolInPlay().size();
            selectSeriesCache -= 1;
            return true;
        }

        return false;
    }

    public void CommitChanges()
    {
        GR.Animator.Dungeon.Loadouts.clear();
        for (AbstractCard card : selectedCards)
        {
            final AnimatorRuntimeLoadout loadout = Find(card);
            if (loadout.IsBeta)
            {
                // Do not unlock trophies or ascension
                Settings.seedSet = true;
            }

            GR.Animator.Dungeon.AddLoadout(loadout);
        }

        if (GR.Animator.Data.SelectedLoadout.IsBeta)
        {
            Settings.seedSet = true;
        }

        if (GameUtilities.IsPlayerClass(GR.Animator.PlayerClass) && GameUtilities.IsNormalRun(false) && Settings.seed != null)
        {
            GR.Animator.Config.LastSeed.Set(Settings.seed.toString(), true);
        }

        GR.Animator.Dungeon.InitializeCardPool(false);
    }

    public ArrayList<AbstractCard> GetAllCardsInPool()
    {
        final ArrayList<AbstractCard> cards = new ArrayList<>();
        for (AbstractCard card : selectedCards)
        {
            cards.addAll(Find(card).GetCardPoolInPlay().values());
        }

        return cards;
    }
}
