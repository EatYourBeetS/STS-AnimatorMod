package eatyourbeets.resources.animator.loadouts;

import eatyourbeets.cards.base.AnimatorCard_UltraRare;
import eatyourbeets.resources.animator.misc.AnimatorLoadout;
import eatyourbeets.resources.animator.misc.AnimatorTrophies;
import eatyourbeets.ui.animator.characterSelection.AnimatorLoadoutRenderer;

import java.util.ArrayList;

public class _Test extends AnimatorLoadout
{
    private final String cardID;

    public _Test(eatyourbeets.cards.base.Synergy synergy, String cardID, int amount)
    {
        super(synergy);

        this.cardID = cardID;
        this.IsBeta = true;
        this.ID = -1;
        this.StartingGold = 0;
        this.MaxHP = 0;
    }

    @Override
    public AnimatorCard_UltraRare GetUltraRare()
    {
        return null;
    }

    @Override
    public ArrayList<String> GetStartingDeck()
    {
        return new ArrayList<>();
    }

    @Override
    public String GetSymbolicCardID()
    {
        return cardID;
    }

    @Override
    public String GetTrophyMessage(int trophy)
    {
        return null;
    }

    public void OnVictory(AnimatorLoadoutRenderer currentLoadout, int ascensionLevel)
    {
        //
    }

    @Override
    public AnimatorTrophies GetTrophies()
    {
        return null;
    }
}