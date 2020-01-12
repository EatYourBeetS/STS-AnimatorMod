package eatyourbeets.resources.animator.loadouts;

import eatyourbeets.cards.animator.basic.Defend;
import eatyourbeets.cards.animator.basic.Defend_Elsword;
import eatyourbeets.cards.animator.basic.Strike;
import eatyourbeets.cards.animator.basic.Strike_Elsword;
import eatyourbeets.cards.animator.series.Elsword.Chung;
import eatyourbeets.cards.animator.series.Elsword.Eve;
import eatyourbeets.cards.animator.series.Elsword.Raven;
import eatyourbeets.cards.animator.ultrarare.Rose;
import eatyourbeets.cards.base.AnimatorCard_UltraRare;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.resources.animator.metrics.AnimatorLoadout;

import java.util.ArrayList;

public class Elsword extends AnimatorLoadout
{
    public Elsword()
    {
        super(Synergies.Elsword);
    }

    @Override
    public ArrayList<String> GetStartingDeck()
    {
        if (startingDeck.isEmpty())
        {
            startingDeck.add(Strike_Elsword.ID);
            startingDeck.add(Defend_Elsword.ID);
            startingDeck.add(Strike.ID);
            startingDeck.add(Strike.ID);
            startingDeck.add(Strike.ID);
            startingDeck.add(Defend.ID);
            startingDeck.add(Defend.ID);
            startingDeck.add(Defend.ID);
            startingDeck.add(Chung.ID);
            startingDeck.add(Raven.ID);
        }

        return startingDeck;
    }

    @Override
    public String GetSymbolicCardID()
    {
        return Eve.ID;
    }

    @Override
    public AnimatorCard_UltraRare GetUltraRare()
    {
        if (ultraRare == null)
        {
            ultraRare = new Rose();
        }

        return ultraRare;
    }
}
