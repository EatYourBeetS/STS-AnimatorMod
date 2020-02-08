package eatyourbeets.resources.animator.loadouts;

import eatyourbeets.cards.animator.basic.Defend;
import eatyourbeets.cards.animator.basic.Defend_OnePunchMan;
import eatyourbeets.cards.animator.basic.Strike;
import eatyourbeets.cards.animator.basic.Strike_OnePunchMan;
import eatyourbeets.cards.animator.series.OnePunchMan.Genos;
import eatyourbeets.cards.animator.series.OnePunchMan.MumenRider;
import eatyourbeets.cards.animator.series.OnePunchMan.Saitama;
import eatyourbeets.cards.animator.ultrarare.SeriousSaitama;
import eatyourbeets.cards.base.AnimatorCard_UltraRare;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.resources.animator.misc.AnimatorLoadout;

import java.util.ArrayList;

public class OnePunchMan extends AnimatorLoadout
{
    public OnePunchMan()
    {
        super(Synergies.OnePunchMan);
    }

    @Override
    public ArrayList<String> GetStartingDeck()
    {
        if (startingDeck.isEmpty())
        {
            startingDeck.add(Strike_OnePunchMan.ID);
            startingDeck.add(Defend_OnePunchMan.ID);
            startingDeck.add(Strike.DATA.ID);
            startingDeck.add(Strike.DATA.ID);
            startingDeck.add(Strike.DATA.ID);
            startingDeck.add(Defend.DATA.ID);
            startingDeck.add(Defend.DATA.ID);
            startingDeck.add(Defend.DATA.ID);
            startingDeck.add(Genos.DATA.ID);
            startingDeck.add(MumenRider.DATA.ID);
        }

        return startingDeck;
    }

    @Override
    public String GetSymbolicCardID()
    {
        return Saitama.DATA.ID;
    }

    @Override
    public AnimatorCard_UltraRare GetUltraRare()
    {
        if (ultraRare == null)
        {
            ultraRare = new SeriousSaitama();
        }

        return ultraRare;
    }
}
