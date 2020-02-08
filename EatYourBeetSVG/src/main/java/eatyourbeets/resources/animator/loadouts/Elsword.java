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
import eatyourbeets.resources.animator.misc.AnimatorLoadout;

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
            startingDeck.add(Strike.DATA.ID);
            startingDeck.add(Strike.DATA.ID);
            startingDeck.add(Strike.DATA.ID);
            startingDeck.add(Defend.DATA.ID);
            startingDeck.add(Defend.DATA.ID);
            startingDeck.add(Defend.DATA.ID);
            startingDeck.add(Chung.DATA.ID);
            startingDeck.add(Raven.DATA.ID);
        }

        return startingDeck;
    }

    @Override
    public String GetSymbolicCardID()
    {
        return Eve.DATA.ID;
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
