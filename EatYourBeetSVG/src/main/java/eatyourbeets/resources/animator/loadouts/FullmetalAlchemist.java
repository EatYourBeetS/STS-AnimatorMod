package eatyourbeets.resources.animator.loadouts;

import eatyourbeets.cards.animator.basic.Defend;
import eatyourbeets.cards.animator.basic.Defend_FullmetalAlchemist;
import eatyourbeets.cards.animator.basic.Strike;
import eatyourbeets.cards.animator.basic.Strike_FullmetalAlchemist;
import eatyourbeets.cards.animator.series.FullmetalAlchemist.ElricAlphonse;
import eatyourbeets.cards.animator.series.FullmetalAlchemist.ElricEdward;
import eatyourbeets.cards.animator.series.FullmetalAlchemist.RoyMustang;
import eatyourbeets.cards.animator.ultrarare.Truth;
import eatyourbeets.cards.base.AnimatorCard_UltraRare;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.resources.animator.metrics.AnimatorLoadout;

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
            startingDeck.add(Strike.ID);
            startingDeck.add(Strike.ID);
            startingDeck.add(Strike.ID);
            startingDeck.add(Defend.ID);
            startingDeck.add(Defend.ID);
            startingDeck.add(Defend.ID);
            startingDeck.add(ElricEdward.ID);
            startingDeck.add(ElricAlphonse.ID);
        }

        return startingDeck;
    }

    @Override
    public String GetSymbolicCardID()
    {
        return RoyMustang.ID;
    }

    @Override
    public AnimatorCard_UltraRare GetUltraRare()
    {
        if (ultraRare == null)
        {
            ultraRare = new Truth();
        }

        return ultraRare;
    }
}
