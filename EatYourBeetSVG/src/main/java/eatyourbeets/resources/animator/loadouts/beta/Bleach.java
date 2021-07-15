package eatyourbeets.resources.animator.loadouts.beta;

import eatyourbeets.cards.animator.basic.Defend;
import eatyourbeets.cards.animator.basic.Strike;
import eatyourbeets.cards.animator.beta.basic.Defend_Bleach;
import eatyourbeets.cards.animator.beta.basic.Strike_Bleach;
import eatyourbeets.cards.animator.beta.series.Bleach.IchigoKurosaki;
import eatyourbeets.cards.animator.beta.series.Bleach.RenjiAbarai;
import eatyourbeets.cards.animator.beta.series.Bleach.UryuuIshida;
import eatyourbeets.cards.animator.beta.ultrarare.SosukeAizen;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.resources.animator.misc.AnimatorLoadout;

import java.util.ArrayList;

public class Bleach extends AnimatorLoadout
{
    public Bleach()
    {
        super(CardSeries.Bleach);

        IsBeta = true;
    }

    @Override
    public ArrayList<String> GetStartingDeck()
    {
        if (startingDeck.isEmpty())
        {
            startingDeck.add(Strike_Bleach.ID);
            startingDeck.add(Defend_Bleach.ID);
            startingDeck.add(Strike.DATA.ID);
            startingDeck.add(Strike.DATA.ID);
            startingDeck.add(Strike.DATA.ID);
            startingDeck.add(Defend.DATA.ID);
            startingDeck.add(Defend.DATA.ID);
            startingDeck.add(Defend.DATA.ID);
            startingDeck.add(RenjiAbarai.DATA.ID);
            startingDeck.add(UryuuIshida.DATA.ID);
        }

        return startingDeck;
    }

    @Override
    public EYBCardData GetSymbolicCard()
    {
        return IchigoKurosaki.DATA;
    }

    @Override
    public EYBCardData GetUltraRare()
    {
        return SosukeAizen.DATA;
    }
}
