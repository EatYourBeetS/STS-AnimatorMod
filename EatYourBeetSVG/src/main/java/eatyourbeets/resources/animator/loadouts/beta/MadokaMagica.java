package eatyourbeets.resources.animator.loadouts.beta;

import eatyourbeets.cards.animator.basic.*;
import eatyourbeets.cards.animator.beta.MadokaMagica.*;
import eatyourbeets.cards.base.AnimatorCard_UltraRare;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.resources.animator.misc.AnimatorLoadout;

import java.util.ArrayList;

public class MadokaMagica extends AnimatorLoadout
{
    public MadokaMagica()
    {
        super(Synergies.MadokaMagica);

        IsBeta = true;
    }

    @Override
    public ArrayList<String> GetStartingDeck()
    {
        if (startingDeck.isEmpty())
        {
            startingDeck.add(Strike_MadokaMagica.ID);
            startingDeck.add(Defend_MadokaMagica.ID);
            startingDeck.add(Strike.DATA.ID);
            startingDeck.add(Strike.DATA.ID);
            startingDeck.add(Strike.DATA.ID);
            startingDeck.add(Defend.DATA.ID);
            startingDeck.add(Defend.DATA.ID);
            startingDeck.add(Defend.DATA.ID);
            startingDeck.add(IrohaTamaki.DATA.ID);
            startingDeck.add(YuiTsuruno.DATA.ID);
        }

        return startingDeck;
    }

    @Override
    public String GetSymbolicCardID()
    {
        return Kyubey.DATA.ID;
    }

    @Override
    public AnimatorCard_UltraRare GetUltraRare()
    {
        return new Walpurgisnacht();
    }
}
