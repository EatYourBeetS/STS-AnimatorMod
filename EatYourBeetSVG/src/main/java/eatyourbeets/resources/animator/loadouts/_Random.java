package eatyourbeets.resources.animator.loadouts;

import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.resources.animator.misc.AnimatorLoadout;
import eatyourbeets.resources.animator.misc.AnimatorTrophies;
import eatyourbeets.ui.animator.characterSelection.AnimatorLoadoutRenderer;

import java.util.ArrayList;

public class _Random extends AnimatorLoadout
{
    public _Random()
    {
        super("Random");

        this.ID = 0;
        this.StartingGold = 0;
        this.MaxHP = 0;
    }

    @Override
    public ArrayList<String> GetStartingDeck()
    {
        return new ArrayList<>();
    }

    @Override
    public EYBCardData GetUltraRare()
    {
        return null;
    }

    @Override
    public EYBCardData GetSymbolicCard()
    {
        return null;
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