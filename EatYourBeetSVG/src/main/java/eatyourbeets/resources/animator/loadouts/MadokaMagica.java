package eatyourbeets.resources.animator.loadouts;

import eatyourbeets.cards.animator.basic.Defend;
import eatyourbeets.cards.animator.basic.Defend_MadokaMagica;
import eatyourbeets.cards.animator.basic.Strike;
import eatyourbeets.cards.animator.basic.Strike_MadokaMagica;
import eatyourbeets.cards.animator.series.MadokaMagica.*;
import eatyourbeets.cards.animator.ultrarare.Walpurgisnacht;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.resources.animator.misc.AnimatorLoadout;

import java.util.ArrayList;

public class MadokaMagica extends AnimatorLoadout
{
    public MadokaMagica()
    {
        super(Synergies.MadokaMagica);
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
    public EYBCardData GetSymbolicCard()
    {
        return Kyubey.DATA;
    }

    @Override
    public EYBCardData GetUltraRare()
    {
        return Walpurgisnacht.DATA;
    }
}
