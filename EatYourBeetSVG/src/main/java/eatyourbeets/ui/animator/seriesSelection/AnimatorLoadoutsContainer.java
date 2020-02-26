package eatyourbeets.ui.animator.seriesSelection;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.random.Random;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.animator.misc.AnimatorLoadout;
import eatyourbeets.resources.animator.misc.AnimatorRuntimeLoadout;
import eatyourbeets.utilities.RandomizedList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class AnimatorLoadoutsContainer
{
    public int TotalCardsInPool = 0;
    public final Map<AbstractCard, AnimatorRuntimeLoadout> cardsMap = new HashMap<>();
    public final ArrayList<AbstractCard> promotedCards = new ArrayList<>();
    public final ArrayList<AbstractCard> selectedCards = new ArrayList<>();
    public final ArrayList<AbstractCard> betaCards = new ArrayList<>();
    public final ArrayList<AbstractCard> allCards = new ArrayList<>();

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

        int promotedCount = 0;
        RandomizedList<AnimatorRuntimeLoadout> toPromote = new RandomizedList<>();
        ArrayList<AnimatorRuntimeLoadout> seriesSelectionItems = new ArrayList<>();
        for (AnimatorLoadout loadout : GR.Animator.Data.BaseLoadouts)
        {
            AnimatorRuntimeLoadout card = AnimatorRuntimeLoadout.TryCreate(loadout);
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
            AnimatorRuntimeLoadout card = AnimatorRuntimeLoadout.TryCreate(loadout);
            if (card != null)
            {
                seriesSelectionItems.add(card);
            }
        }

        Random rng = new Random(Settings.seed + 13);
        while (promotedCount < 3)
        {
            toPromote.Retrieve(rng).Promote();
            promotedCount += 1;
        }

        for (AnimatorRuntimeLoadout c : seriesSelectionItems)
        {
            cardsMap.put(c.BuildCard(), c);
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

    public boolean Select(AbstractCard card)
    {
        if (!selectedCards.contains(card))
        {
            TotalCardsInPool += Find(card).Cards.size();
            selectedCards.add(card);
            return true;
        }

        return false;
    }

    public boolean Deselect(AbstractCard card)
    {
        AnimatorRuntimeLoadout c = Find(card);
        if (!c.promoted && selectedCards.remove(card))
        {
            TotalCardsInPool -= c.Cards.size();
            return true;
        }

        return false;
    }

    public void CommitChanges()
    {
        GR.Animator.Dungeon.Series.clear();
        for (AbstractCard card : selectedCards)
        {
            AnimatorRuntimeLoadout loadout = Find(card);

            if (loadout.IsBeta)
            {
                // Do not unlock trophies or ascension
                Settings.seedSet = true;
            }

            GR.Animator.Dungeon.AddSeries(loadout);
        }

        GR.Animator.Dungeon.InitializeCardPool(false);
    }
}
