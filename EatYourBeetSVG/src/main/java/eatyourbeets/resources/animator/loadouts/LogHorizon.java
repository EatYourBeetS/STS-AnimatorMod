package eatyourbeets.resources.animator.loadouts;

import eatyourbeets.cards.animator.basic.Defend;
import eatyourbeets.cards.animator.basic.Strike;
import eatyourbeets.cards.animator.basic.Defend_LogHorizon;
import eatyourbeets.cards.animator.basic.Strike_LogHorizon;
import eatyourbeets.cards.animator.series.LogHorizon.*;
import eatyourbeets.cards.animator.ultrarare.Krusty;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.resources.animator.misc.AnimatorLoadout;

import java.util.ArrayList;

public class LogHorizon extends AnimatorLoadout
{
    public LogHorizon()
    {
        super(Synergies.LogHorizon);
    }

    @Override
    public ArrayList<String> GetStartingDeck()
    {
        if (startingDeck.isEmpty())
        {
            startingDeck.add(Strike_LogHorizon.ID);
            startingDeck.add(Defend_LogHorizon.ID);
            startingDeck.add(Strike.DATA.ID);
            startingDeck.add(Strike.DATA.ID);
            startingDeck.add(Strike.DATA.ID);
            startingDeck.add(Defend.DATA.ID);
            startingDeck.add(Defend.DATA.ID);
            startingDeck.add(Defend.DATA.ID);
            startingDeck.add(HousakiTohya.DATA.ID);
            startingDeck.add(HousakiMinori.DATA.ID);
        }

        return startingDeck;
    }

    @Override
    public EYBCardData GetSymbolicCard()
    {
        return Akatsuki.DATA;
    }

    @Override
    public EYBCardData GetUltraRare()
    {
        return Krusty.DATA;
    }
}
