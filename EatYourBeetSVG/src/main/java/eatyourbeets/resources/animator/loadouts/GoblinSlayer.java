package eatyourbeets.resources.animator.loadouts;

import eatyourbeets.cards.animator.basic.Defend;
import eatyourbeets.cards.animator.basic.Defend_GoblinSlayer;
import eatyourbeets.cards.animator.basic.Strike;
import eatyourbeets.cards.animator.basic.Strike_GoblinSlayer;
import eatyourbeets.cards.animator.series.GoblinSlayer.DwarfShaman;
import eatyourbeets.cards.animator.series.GoblinSlayer.LizardPriest;
import eatyourbeets.cards.animator.ultrarare.Hero;
import eatyourbeets.cards.base.AnimatorCard_UltraRare;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.resources.animator.misc.AnimatorLoadout;

import java.util.ArrayList;

public class GoblinSlayer extends AnimatorLoadout
{
    public GoblinSlayer()
    {
        super(Synergies.GoblinSlayer);
    }

    @Override
    public ArrayList<String> GetStartingDeck()
    {
        if (startingDeck.isEmpty())
        {
            startingDeck.add(Strike_GoblinSlayer.ID);
            startingDeck.add(Defend_GoblinSlayer.ID);
            startingDeck.add(Strike.DATA.ID);
            startingDeck.add(Strike.DATA.ID);
            startingDeck.add(Strike.DATA.ID);
            startingDeck.add(Defend.DATA.ID);
            startingDeck.add(Defend.DATA.ID);
            startingDeck.add(Defend.DATA.ID);
            startingDeck.add(LizardPriest.DATA.ID);
            startingDeck.add(DwarfShaman.DATA.ID);
        }

        return startingDeck;
    }

    @Override
    public String GetSymbolicCardID()
    {
        return eatyourbeets.cards.animator.series.GoblinSlayer.GoblinSlayer.DATA.ID;
    }

    @Override
    public AnimatorCard_UltraRare GetUltraRare()
    {
        if (ultraRare == null)
        {
            ultraRare = new Hero();
        }

        return ultraRare;
    }
}
