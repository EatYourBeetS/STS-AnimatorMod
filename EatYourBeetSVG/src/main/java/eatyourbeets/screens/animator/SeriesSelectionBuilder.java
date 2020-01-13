package eatyourbeets.screens.animator;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.random.Random;
import eatyourbeets.cards.animator.colorless.rare.Emilia;
import eatyourbeets.cards.animator.colorless.uncommon.QuestionMark;
import eatyourbeets.cards.animator.colorless.uncommon.Urushihara;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.animator.loadouts._Test;
import eatyourbeets.resources.animator.metrics.AnimatorLoadout;
import eatyourbeets.utilities.RandomizedList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class SeriesSelectionBuilder
{
    public int TotalCardsInPool = 0;
    public final ArrayList<AbstractCard> selectedCards = new ArrayList<>();
    public final Map<AbstractCard, SeriesSelectionCard> cardsMap = new HashMap<>();

    public Collection<SeriesSelectionCard> CreateCards()
    {
        TotalCardsInPool = 0;

        cardsMap.clear();
        selectedCards.clear();

        int promotedCount = 0;
        RandomizedList<SeriesSelectionCard> toPromote = new RandomizedList<>();
        ArrayList<SeriesSelectionCard> seriesSelectionCards = new ArrayList<>();
        for (AnimatorLoadout loadout : GR.Animator.Database.BaseLoadouts)
        {
            SeriesSelectionCard card = SeriesSelectionCard.TryCreate(loadout);
            if (card != null)
            {
                if (loadout == GR.Animator.Database.SelectedLoadout)
                {
                    card.Promote();
                    promotedCount += 1;
                }
                else
                {
                    toPromote.Add(card);
                }

                seriesSelectionCards.add(card);
            }
        }

        // <Beta>

        seriesSelectionCards.add(SeriesSelectionCard.TryCreate(new _Test(Synergies.HatarakuMaouSama, Urushihara.ID, 4)));
        seriesSelectionCards.add(SeriesSelectionCard.TryCreate(new _Test(Synergies.ReZero, Emilia.ID, 5)));
        seriesSelectionCards.add(SeriesSelectionCard.TryCreate(new _Test(Synergies.Jojo, QuestionMark.ID, 7)));

        // </Beta>


        Random rng = new Random(Settings.seed + 13);
        while (promotedCount < 3)
        {
            toPromote.Retrieve(rng).Promote();
            promotedCount += 1;
        }

        for (SeriesSelectionCard c : seriesSelectionCards)
        {
            AbstractCard card = c.BuildCard();
            cardsMap.put(card, c);
            Select(card);
        }

        return cardsMap.values();
    }

    public SeriesSelectionCard Find(AbstractCard card)
    {
        return cardsMap.get(card);
    }

    public Collection<SeriesSelectionCard> GetAllCards()
    {
        return cardsMap.values();
    }

    public boolean Select(AbstractCard card)
    {
        if (!selectedCards.contains(card))
        {
            TotalCardsInPool += Find(card).size;
            selectedCards.add(card);
            return true;
        }

        return false;
    }

    public boolean Deselect(AbstractCard card)
    {
        SeriesSelectionCard c = Find(card);
        if (!c.promoted && selectedCards.remove(card))
        {
            TotalCardsInPool -= c.size;
            return true;
        }

        return false;
    }

    public void UpdateDatabase()
    {
        // TODO:
    }
}
