package eatyourbeets.resources.animator.loadouts.beta;

import eatyourbeets.cards.animator.basic.Defend;
import eatyourbeets.cards.animator.basic.Strike;
import eatyourbeets.cards.animator.beta.basic.Defend_RozenMaiden;
import eatyourbeets.cards.animator.beta.basic.Strike_RozenMaiden;
import eatyourbeets.cards.animator.beta.series.RozenMaiden.Hinaichigo;
import eatyourbeets.cards.animator.beta.series.RozenMaiden.Shinku;
import eatyourbeets.cards.animator.beta.series.RozenMaiden.Souseiseki;
import eatyourbeets.cards.animator.beta.ultrarare.Kirakishou;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.resources.animator.misc.AnimatorLoadout;

import java.util.ArrayList;

public class RozenMaiden extends AnimatorLoadout
{
    public RozenMaiden()
    {
        super(CardSeries.RozenMaiden);
        IsBeta = true;
    }

    @Override
    public ArrayList<String> GetStartingDeck()
    {
        if (startingDeck.isEmpty())
        {
            startingDeck.add(Strike_RozenMaiden.ID);
            startingDeck.add(Defend_RozenMaiden.ID);
            startingDeck.add(Strike.DATA.ID);
            startingDeck.add(Strike.DATA.ID);
            startingDeck.add(Strike.DATA.ID);
            startingDeck.add(Defend.DATA.ID);
            startingDeck.add(Defend.DATA.ID);
            startingDeck.add(Defend.DATA.ID);
            startingDeck.add(Souseiseki.DATA.ID);
            startingDeck.add(Hinaichigo.DATA.ID);
        }

        return startingDeck;
    }

    @Override
    public EYBCardData GetSymbolicCard()
    {
        return Shinku.DATA;
    }

    @Override
    public EYBCardData GetUltraRare()
    {
        return Kirakishou.DATA;
    }
}
