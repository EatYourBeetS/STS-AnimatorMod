package pinacolada.ui.seriesSelection;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import eatyourbeets.utilities.Mathf;
import pinacolada.resources.GR;
import pinacolada.resources.pcl.misc.PCLLoadout;
import pinacolada.resources.pcl.misc.PCLRuntimeLoadout;
import pinacolada.utilities.PCLJUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class PCLLoadoutsContainer
{
    public static final int MINIMUM_SERIES = 10;

    public int CurrentSeriesLimit;
    public int TotalCardsInPool = 0;
    public final Map<AbstractCard, PCLRuntimeLoadout> cardsMap = new HashMap<>();
    public final ArrayList<AbstractCard> currentCards = new ArrayList<>();
    public final ArrayList<AbstractCard> expandedCards = new ArrayList<>();
    public final ArrayList<AbstractCard> betaCards = new ArrayList<>();
    public final ArrayList<AbstractCard> allCards = new ArrayList<>();
    public AbstractCard currentSeriesCard;

    public static void PreloadResources()
    {
        CardCrawlGame.sound.preload("CARD_SELECT");
        for (PCLLoadout loadout : GR.PCL.Data.BaseLoadouts)
        {
            PCLRuntimeLoadout temp = PCLRuntimeLoadout.TryCreate(loadout);
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

        final ArrayList<PCLRuntimeLoadout> seriesSelectionItems = new ArrayList<>();
        for (PCLLoadout loadout : GR.PCL.Data.BaseLoadouts)
        {
            final PCLRuntimeLoadout card = PCLRuntimeLoadout.TryCreate(loadout);
            if (card != null)
            {
                seriesSelectionItems.add(card);
            }
        }

        for (PCLLoadout loadout : GR.PCL.Data.BetaLoadouts)
        {
            final PCLRuntimeLoadout card = PCLRuntimeLoadout.TryCreate(loadout);
            if (card != null)
            {
                seriesSelectionItems.add(card);
            }
        }

        for (PCLRuntimeLoadout c : seriesSelectionItems)
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

                if (GR.PCL.Config.SelectedSeries.Get().contains(c.Loadout.Series)) {
                    TotalCardsInPool += c.GetCardPoolInPlay().size();
                    currentCards.add(card);
                    card.targetTransparency = 1f;
                }
                else {
                    card.targetTransparency = 0.5f;
                }

                if (GR.PCL.Config.ExpandedSeries.Get().contains(c.Loadout.Series)) {
                    expandedCards.add(card);
                    c.ToggleExpansion(true);
                }

                if (c.Loadout.Series.equals(GR.PCL.Data.SelectedLoadout.Series)) {
                    currentSeriesCard = card;
                    card.rarity = AbstractCard.CardRarity.RARE;
                    card.beginGlowing();
                }
            }
            else
            {
                PCLJUtils.LogError(this, "AnimatorRuntimeLoadout.BuildCard() failed, " + c.Loadout.Name);
            }
        }

        CurrentSeriesLimit = Mathf.Clamp(GR.PCL.Config.SeriesSize.Get(), MINIMUM_SERIES, currentCards.size());
    }

    public PCLRuntimeLoadout Find(AbstractCard card)
    {
        return cardsMap.get(card);
    }

    public Collection<PCLRuntimeLoadout> GetAllCards()
    {
        return cardsMap.values();
    }

    public boolean ToggleExpansion(AbstractCard card)
    {
        PCLRuntimeLoadout c = Find(card);
        return ToggleExpansion(card, c, !c.expansionEnabled);
    }

    public boolean ToggleExpansion(AbstractCard card, boolean value)
    {
        PCLRuntimeLoadout c = Find(card);
        return ToggleExpansion(card, c, value);
    }

    public boolean ToggleExpansion(AbstractCard card, PCLRuntimeLoadout c, boolean value)
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
        PCLRuntimeLoadout c = Find(card);
        if (currentCards.remove(card))
        {
            TotalCardsInPool -= c.GetCardPoolInPlay().size();
            card.targetTransparency = 0.5f;
            CurrentSeriesLimit = Mathf.Max(MINIMUM_SERIES, Mathf.Min(CurrentSeriesLimit, currentCards.size()));
            return true;
        }

        return false;
    }

    public void CommitChanges()
    {
        GR.PCL.Data.SelectedLoadout = Find(currentSeriesCard).Loadout;
        GR.PCL.Config.SelectedSeries.Set(PCLJUtils.Map(currentCards, card -> Find(card).Loadout.Series), true);
        GR.PCL.Config.ExpandedSeries.Set(PCLJUtils.Map(expandedCards, card -> Find(card).Loadout.Series), true);
        GR.PCL.Config.SeriesSize.Set(Mathf.Max(MINIMUM_SERIES, CurrentSeriesLimit), true);

        PCLJUtils.LogInfo(this, "Selected Loadout: " + GR.PCL.Data.SelectedLoadout.Series);
        PCLJUtils.LogInfo(this, "Selected Series: " + PCLJUtils.JoinStrings(",", GR.PCL.Config.SelectedSeries.Get()));
        PCLJUtils.LogInfo(this, "Series Size: " + GR.PCL.Config.SeriesSize.Get());
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
