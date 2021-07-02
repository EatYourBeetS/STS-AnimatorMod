package eatyourbeets.resources.animator.loadouts.beta;

import eatyourbeets.cards.animator.basic.Defend;
import eatyourbeets.cards.animator.basic.Strike;
import eatyourbeets.cards.animator.beta.basic.Defend_GenshinImpact;
import eatyourbeets.cards.animator.beta.basic.Strike_GenshinImpact;
import eatyourbeets.cards.animator.beta.colorless.*;
import eatyourbeets.cards.animator.beta.series.GenshinImpact.*;
import eatyourbeets.cards.animator.beta.ultrarare.YuyukoSaigyouji;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.resources.animator.misc.AnimatorLoadout;

import java.util.ArrayList;

public class GenshinImpact extends AnimatorLoadout
{
    public GenshinImpact()
    {
        super(Synergies.GenshinImpact);
        IsBeta = true;
    }

    @Override
    public ArrayList<String> GetStartingDeck()
    {
        if (startingDeck.isEmpty())
        {
            startingDeck.add(Strike_GenshinImpact.ID);
            startingDeck.add(Defend_GenshinImpact.ID);
            startingDeck.add(Strike.DATA.ID);
            startingDeck.add(Strike.DATA.ID);
            startingDeck.add(Strike.DATA.ID);
            startingDeck.add(Defend.DATA.ID);
            startingDeck.add(Defend.DATA.ID);
            startingDeck.add(Defend.DATA.ID);
            startingDeck.add(Amber.DATA.ID);
            startingDeck.add(Noelle.DATA.ID);
        }

        return startingDeck;
    }

    @Override
    public EYBCardData GetSymbolicCard()
    {
        return Venti.DATA;
    }

    @Override
    public EYBCardData GetUltraRare()
    {
        return Traveler.DATA;
    }
}
