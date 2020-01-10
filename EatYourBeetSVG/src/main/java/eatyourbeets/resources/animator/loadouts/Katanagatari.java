package eatyourbeets.resources.animator.loadouts;

import eatyourbeets.cards.animator.basic.Defend;
import eatyourbeets.cards.animator.basic.Defend_Katanagatari;
import eatyourbeets.cards.animator.basic.Strike;
import eatyourbeets.cards.animator.basic.Strike_Katanagatari;
import eatyourbeets.cards.animator.series.Katanagatari.Azekura;
import eatyourbeets.cards.animator.series.Katanagatari.Emonzaemon;
import eatyourbeets.cards.animator.series.Katanagatari.Togame;
import eatyourbeets.cards.animator.ultrarare.ShikizakiKiki;
import eatyourbeets.cards.base.AnimatorCard_UltraRare;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.resources.animator.metrics.AnimatorLoadout;

import java.util.ArrayList;

public class Katanagatari extends AnimatorLoadout
{
    public Katanagatari()
    {
        super(Synergies.Katanagatari);
    }

    @Override
    public ArrayList<String> GetStartingDeck()
    {
        if (startingDeck.isEmpty())
        {
            startingDeck.add(Strike_Katanagatari.ID);
            startingDeck.add(Defend_Katanagatari.ID);
            startingDeck.add(Strike.ID);
            startingDeck.add(Strike.ID);
            startingDeck.add(Strike.ID);
            startingDeck.add(Defend.ID);
            startingDeck.add(Defend.ID);
            startingDeck.add(Defend.ID);
            startingDeck.add(Azekura.ID);
            startingDeck.add(Emonzaemon.ID);
        }

        return startingDeck;
    }

    @Override
    public String GetRepresentativeCard()
    {
        return Togame.ID;
    }

    @Override
    public AnimatorCard_UltraRare GetUltraRare()
    {
        if (ultraRare == null)
        {
            ultraRare = new ShikizakiKiki();
        }

        return ultraRare;
    }
}
