package eatyourbeets.resources.animator.loadouts;

import eatyourbeets.cards.animator.basic.Defend;
import eatyourbeets.cards.animator.basic.Defend_TenSura;
import eatyourbeets.cards.animator.basic.Strike;
import eatyourbeets.cards.animator.basic.Strike_TenSura;
import eatyourbeets.cards.animator.series.TenseiSlime.Benimaru;
import eatyourbeets.cards.animator.series.TenseiSlime.Rimuru;
import eatyourbeets.cards.animator.series.TenseiSlime.Shuna;
import eatyourbeets.cards.animator.ultrarare.Veldora;
import eatyourbeets.cards.base.AnimatorCard_UltraRare;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.resources.animator.misc.AnimatorLoadout;

import java.util.ArrayList;

public class TenSura extends AnimatorLoadout
{
    public TenSura()
    {
        super(Synergies.TenSura);
    }

    @Override
    public ArrayList<String> GetStartingDeck()
    {
        if (startingDeck.isEmpty())
        {
            startingDeck.add(Strike_TenSura.ID);
            startingDeck.add(Defend_TenSura.ID);
            startingDeck.add(Strike.ID);
            startingDeck.add(Strike.ID);
            startingDeck.add(Strike.ID);
            startingDeck.add(Defend.ID);
            startingDeck.add(Defend.ID);
            startingDeck.add(Defend.ID);
            startingDeck.add(Benimaru.ID);
            startingDeck.add(Shuna.ID);
        }

        return startingDeck;
    }

    @Override
    public String GetSymbolicCardID()
    {
        return Rimuru.ID;
    }

    @Override
    public AnimatorCard_UltraRare GetUltraRare()
    {
        if (ultraRare == null)
        {
            ultraRare = new Veldora();
        }

        return ultraRare;
    }
}
