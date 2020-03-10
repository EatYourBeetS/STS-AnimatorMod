package eatyourbeets.resources.animator.loadouts;

import eatyourbeets.cards.animator.basic.Defend;
import eatyourbeets.cards.animator.basic.Defend_FullmetalAlchemist;
import eatyourbeets.cards.animator.basic.Strike;
import eatyourbeets.cards.animator.basic.Strike_FullmetalAlchemist;
import eatyourbeets.cards.animator.series.FullmetalAlchemist.ElricAlphonse;
import eatyourbeets.cards.animator.series.FullmetalAlchemist.ElricEdward;
import eatyourbeets.cards.animator.series.FullmetalAlchemist.RoyMustang;
import eatyourbeets.cards.animator.ultrarare.Truth;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.resources.animator.misc.AnimatorLoadout;

import java.util.ArrayList;

public class FullmetalAlchemist extends AnimatorLoadout
{
    public FullmetalAlchemist()
    {
        super(Synergies.FullmetalAlchemist);
    }

    @Override
    public ArrayList<String> GetStartingDeck()
    {
        if (startingDeck.isEmpty())
        {
            startingDeck.add(Strike_FullmetalAlchemist.ID);
            startingDeck.add(Defend_FullmetalAlchemist.ID);
            startingDeck.add(Strike.DATA.ID);
            startingDeck.add(Strike.DATA.ID);
            startingDeck.add(Strike.DATA.ID);
            startingDeck.add(Defend.DATA.ID);
            startingDeck.add(Defend.DATA.ID);
            startingDeck.add(Defend.DATA.ID);
            startingDeck.add(ElricEdward.DATA.ID);
            startingDeck.add(ElricAlphonse.DATA.ID);
        }

        return startingDeck;
    }

    @Override
    public EYBCardData GetSymbolicCard()
    {
        return RoyMustang.DATA;
    }

    @Override
    public EYBCardData GetUltraRare()
    {
        return Truth.DATA;
    }
}
