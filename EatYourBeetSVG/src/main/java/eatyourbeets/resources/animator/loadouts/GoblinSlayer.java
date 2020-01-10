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
import eatyourbeets.resources.animator.metrics.AnimatorLoadout;

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
            startingDeck.add(Strike.ID);
            startingDeck.add(Strike.ID);
            startingDeck.add(Strike.ID);
            startingDeck.add(Defend.ID);
            startingDeck.add(Defend.ID);
            startingDeck.add(Defend.ID);
            startingDeck.add(LizardPriest.ID);
            startingDeck.add(DwarfShaman.ID);
        }

        return startingDeck;
    }

    @Override
    public String GetRepresentativeCard()
    {
        return eatyourbeets.cards.animator.series.GoblinSlayer.GoblinSlayer.ID;
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
