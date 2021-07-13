package eatyourbeets.resources.animator.loadouts;

import eatyourbeets.cards.animator.basic.Defend;
import eatyourbeets.cards.animator.basic.Defend_TenSura;
import eatyourbeets.cards.animator.basic.Strike;
import eatyourbeets.cards.animator.basic.Strike_TenSura;
import eatyourbeets.cards.animator.series.TenseiSlime.Benimaru;
import eatyourbeets.cards.animator.series.TenseiSlime.Rimuru;
import eatyourbeets.cards.animator.series.TenseiSlime.Shuna;
import eatyourbeets.cards.animator.ultrarare.Veldora;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.resources.animator.misc.AnimatorLoadout;

import java.util.ArrayList;

public class TenSura extends AnimatorLoadout
{
    public TenSura()
    {
        super(CardSeries.TenseiSlime);
    }

    @Override
    public ArrayList<String> GetStartingDeck()
    {
        if (startingDeck.isEmpty())
        {
            startingDeck.add(Strike_TenSura.ID);
            startingDeck.add(Defend_TenSura.ID);
            startingDeck.add(Strike.DATA.ID);
            startingDeck.add(Strike.DATA.ID);
            startingDeck.add(Strike.DATA.ID);
            startingDeck.add(Defend.DATA.ID);
            startingDeck.add(Defend.DATA.ID);
            startingDeck.add(Defend.DATA.ID);
            startingDeck.add(Benimaru.DATA.ID);
            startingDeck.add(Shuna.DATA.ID);
        }

        return startingDeck;
    }

    @Override
    public EYBCardData GetSymbolicCard()
    {
        return Rimuru.DATA;
    }

    @Override
    public EYBCardData GetUltraRare()
    {
        return Veldora.DATA;
    }
}
