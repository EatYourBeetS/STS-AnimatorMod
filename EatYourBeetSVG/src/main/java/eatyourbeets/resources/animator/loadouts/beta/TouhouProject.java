package eatyourbeets.resources.animator.loadouts.beta;

import eatyourbeets.cards.animator.basic.Defend;
import eatyourbeets.cards.animator.basic.Strike;
import eatyourbeets.cards.animator.beta.TouhouProject.*;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.resources.animator.misc.AnimatorLoadout;

import java.util.ArrayList;

public class TouhouProject extends AnimatorLoadout
{
    public TouhouProject()
    {
        super(Synergies.TouhouProject);
        IsBeta = true;
    }

    @Override
    public ArrayList<String> GetStartingDeck()
    {
        if (startingDeck.isEmpty())
        {
            startingDeck.add(Strike_TouhouProject.ID);
            startingDeck.add(Defend_TouhouProject.ID);
            startingDeck.add(Strike.DATA.ID);
            startingDeck.add(Strike.DATA.ID);
            startingDeck.add(Strike.DATA.ID);
            startingDeck.add(Defend.DATA.ID);
            startingDeck.add(Defend.DATA.ID);
            startingDeck.add(Defend.DATA.ID);
            startingDeck.add(MarisaKirisame.DATA.ID);
            startingDeck.add(ReimuHakurei.DATA.ID);
        }

        return startingDeck;
    }

    @Override
    public EYBCardData GetSymbolicCard()
    {
        return AyaShameimaru.DATA;
    }

    @Override
    public EYBCardData GetUltraRare()
    {
        return YuyukoSaigyouji.DATA;
    }
}
