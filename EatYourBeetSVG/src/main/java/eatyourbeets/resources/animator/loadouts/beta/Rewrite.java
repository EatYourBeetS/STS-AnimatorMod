package eatyourbeets.resources.animator.loadouts.beta;

import eatyourbeets.cards.animator.basic.Defend;
import eatyourbeets.cards.animator.basic.Strike;
import eatyourbeets.cards.animator.beta.basic.Defend_Rewrite;
import eatyourbeets.cards.animator.beta.basic.Strike_Rewrite;
import eatyourbeets.cards.animator.beta.series.Rewrite.Kagari;
import eatyourbeets.cards.animator.beta.series.Rewrite.ShizuruNakatsu;
import eatyourbeets.cards.animator.beta.series.Rewrite.YoshinoHaruhiko;
import eatyourbeets.cards.animator.beta.ultrarare.SakuraKashima;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.resources.animator.misc.AnimatorLoadout;

import java.util.ArrayList;

public class Rewrite extends AnimatorLoadout
{
    public Rewrite()
    {
        super(CardSeries.Rewrite);

        IsBeta = true;
    }

    @Override
    public ArrayList<String> GetStartingDeck()
    {
        if (startingDeck.isEmpty())
        {
            startingDeck.add(Strike_Rewrite.ID);
            startingDeck.add(Defend_Rewrite.ID);
            startingDeck.add(Strike.DATA.ID);
            startingDeck.add(Strike.DATA.ID);
            startingDeck.add(Strike.DATA.ID);
            startingDeck.add(Defend.DATA.ID);
            startingDeck.add(Defend.DATA.ID);
            startingDeck.add(Defend.DATA.ID);
            startingDeck.add(YoshinoHaruhiko.DATA.ID);
            startingDeck.add(ShizuruNakatsu.DATA.ID);
        }

        return startingDeck;
    }

    @Override
    public EYBCardData GetSymbolicCard()
    {
        return Kagari.DATA;
    }

    @Override
    public EYBCardData GetUltraRare()
    {
        return SakuraKashima.DATA;
    }
}
