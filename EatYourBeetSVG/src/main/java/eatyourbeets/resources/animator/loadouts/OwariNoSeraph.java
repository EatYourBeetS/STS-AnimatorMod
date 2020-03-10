package eatyourbeets.resources.animator.loadouts;

import eatyourbeets.cards.animator.basic.Defend;
import eatyourbeets.cards.animator.basic.Defend_OwariNoSeraph;
import eatyourbeets.cards.animator.basic.Strike;
import eatyourbeets.cards.animator.basic.Strike_OwariNoSeraph;
import eatyourbeets.cards.animator.series.OwariNoSeraph.Mikaela;
import eatyourbeets.cards.animator.series.OwariNoSeraph.Shinoa;
import eatyourbeets.cards.animator.series.OwariNoSeraph.Yuuichirou;
import eatyourbeets.cards.animator.ultrarare.HiiragiTenri;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.resources.animator.misc.AnimatorLoadout;

import java.util.ArrayList;

public class OwariNoSeraph extends AnimatorLoadout
{
    public OwariNoSeraph()
    {
        super(Synergies.OwariNoSeraph);
    }

    @Override
    public ArrayList<String> GetStartingDeck()
    {
        if (startingDeck.isEmpty())
        {
            startingDeck.add(Strike_OwariNoSeraph.ID);
            startingDeck.add(Defend_OwariNoSeraph.ID);
            startingDeck.add(Strike.DATA.ID);
            startingDeck.add(Strike.DATA.ID);
            startingDeck.add(Strike.DATA.ID);
            startingDeck.add(Defend.DATA.ID);
            startingDeck.add(Defend.DATA.ID);
            startingDeck.add(Defend.DATA.ID);
            startingDeck.add(Shinoa.DATA.ID);
            startingDeck.add(Mikaela.DATA.ID);
        }

        return startingDeck;
    }

    @Override
    public EYBCardData GetSymbolicCard()
    {
        return Yuuichirou.DATA;
    }

    @Override
    public EYBCardData GetUltraRare()
    {
        return HiiragiTenri.DATA;
    }
}
