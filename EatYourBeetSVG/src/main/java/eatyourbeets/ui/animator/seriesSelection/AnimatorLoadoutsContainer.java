package eatyourbeets.ui.animator.seriesSelection;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.animator.misc.AnimatorLoadout;
import eatyourbeets.resources.animator.misc.AnimatorRuntimeLoadout;
import eatyourbeets.utilities.JUtils;
import eatyourbeets.utilities.Mathf;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class AnimatorLoadoutsContainer
{
    public static final int MINIMUM_SERIES = 10;

    public int CurrentSeriesLimit;
    public int TotalCardsInPool = 0;
    public final Map<AbstractCard, AnimatorRuntimeLoadout> cardsMap = new HashMap<>();
    public final ArrayList<AbstractCard> currentCards = new ArrayList<>();
    public final ArrayList<AbstractCard> expandedCards = new ArrayList<>();
    public final ArrayList<AbstractCard> betaCards = new ArrayList<>();
    public final ArrayList<AbstractCard> allCards = new ArrayList<>();
    public AbstractCard currentSeriesCard;

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
        currentCards.clear();
        expandedCards.clear();
        betaCards.clear();
        allCards.clear();

        final ArrayList<AnimatorRuntimeLoadout> seriesSelectionItems = new ArrayList<>();
        for (AnimatorLoadout loadout : GR.Animator.Data.BaseLoadouts)
        {
            final AnimatorRuntimeLoadout card = AnimatorRuntimeLoadout.TryCreate(loadout);
            if (card != null)
            {
                seriesSelectionItems.add(card);
            }
        }

        for (AnimatorLoadout loadout : GR.Animator.Data.BetaLoadouts)
        {
            final AnimatorRuntimeLoadout card = AnimatorRuntimeLoadout.TryCreate(loadout);
            if (card != null)
            {
                seriesSelectionItems.add(card);
            }
        }

        for (AnimatorRuntimeLoadout c : seriesSelectionItems)
        {
            final AbstractCard card = c.BuildCard();
            if (card != null)
            {
                cardsMap.put(card, c);
                if (c.IsBeta) {
                    betaCards.add(card);
                }
                else {
                    allCards.add(card);
                }

                if (GR.Animator.Config.SelectedSeries.Get().contains(c.Loadout.Series)) {
                    TotalCardsInPool += c.GetCardPoolInPlay().size();
                    currentCards.add(card);
                    card.targetTransparency = 1f;
                }
                else {
                    card.targetTransparency = 0.5f;
                }

                if (GR.Animator.Config.ExpandedSeries.Get().contains(c.Loadout.Series)) {
                    expandedCards.add(card);
                    c.ToggleExpansion(true);
                }

                if (c.Loadout.Series.equals(GR.Animator.Data.SelectedLoadout.Series)) {
                    currentSeriesCard = card;
                    card.rarity = AbstractCard.CardRarity.RARE;
                    card.beginGlowing();
                }
            }
            else
            {
                JUtils.LogError(this, "AnimatorRuntimeLoadout.BuildCard() failed, " + c.Loadout.Name);
            }
        }

        CurrentSeriesLimit = Mathf.Clamp(GR.Animator.Config.SeriesSize.Get(), MINIMUM_SERIES, currentCards.size());
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
        return ToggleExpansion(card, c, !c.expansionEnabled);
    }

    public boolean ToggleExpansion(AbstractCard card, boolean value)
    {
        AnimatorRuntimeLoadout c = Find(card);
        return ToggleExpansion(card, c, value);
    }

    public boolean ToggleExpansion(AbstractCard card, AnimatorRuntimeLoadout c, boolean value)
    {
        if (c.canEnableExpansion) {
            TotalCardsInPool -= c.GetCardPoolInPlay().size();
            c.ToggleExpansion(value);
            TotalCardsInPool += c.GetCardPoolInPlay().size();

            if (!value) {
                expandedCards.remove(card);
            }
            else if (!expandedCards.contains(card)) {
                expandedCards.add(card);
            }
            return true;
        }
        return false;
    }

    public boolean SelectCard(AbstractCard card) {
        if (cardsMap.containsKey(card) && card.type != AbstractCard.CardType.CURSE) {
            currentSeriesCard = card;
            for (AbstractCard c : cardsMap.keySet()) {
                c.stopGlowing();
                c.rarity = AbstractCard.CardRarity.COMMON;
            }
            card.rarity = AbstractCard.CardRarity.RARE;
            card.beginGlowing();
            return true;
        }
        return false;
    }

    public boolean AddToPool(AbstractCard card)
    {
        if (!currentCards.contains(card))
        {
            TotalCardsInPool += Find(card).GetCardPoolInPlay().size();
            currentCards.add(card);
            card.targetTransparency = 1f;
            return true;
        }

        return false;
    }

    public boolean RemoveFromPool(AbstractCard card)
    {
        AnimatorRuntimeLoadout c = Find(card);
        if (currentCards.remove(card))
        {
            TotalCardsInPool -= c.GetCardPoolInPlay().size();
            card.targetTransparency = 0.5f;
            CurrentSeriesLimit = Mathf.Clamp(CurrentSeriesLimit, MINIMUM_SERIES, currentCards.size());
            return true;
        }

        return false;
    }

    public void CommitChanges()
    {
        GR.Animator.Data.SelectedLoadout = Find(currentSeriesCard).Loadout;
        GR.Animator.Config.SelectedSeries.Set(JUtils.Map(currentCards, card -> Find(card).Loadout.Series), true);
        GR.Animator.Config.ExpandedSeries.Set(JUtils.Map(expandedCards, card -> Find(card).Loadout.Series), true);
        GR.Animator.Config.SeriesSize.Set(CurrentSeriesLimit, true);
    }

    public ArrayList<AbstractCard> GetAllCardsInPool()
    {
        final ArrayList<AbstractCard> cards = new ArrayList<>();
        for (AbstractCard card : currentCards)
        {
            cards.addAll(Find(card).GetCardPoolInPlay().values());
        }

        return cards;
    }
}
