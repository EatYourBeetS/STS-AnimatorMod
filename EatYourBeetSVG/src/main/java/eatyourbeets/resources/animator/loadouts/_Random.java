package eatyourbeets.resources.animator.loadouts;

import eatyourbeets.cards.base.AnimatorCard_UltraRare;
import eatyourbeets.resources.animator.metrics.AnimatorLoadout;
import eatyourbeets.resources.animator.metrics.AnimatorTrophies;
import eatyourbeets.ui.screens.animator.characterSelection.AnimatorLoadoutRenderer;

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
        return null;
    }

    @Override
    public String GetTrophyMessage(int trophy)
    {
        return null;
    }

    public void OnVictory(AnimatorLoadoutRenderer currentLoadout, int ascensionLevel)
    {

    }

    @Override
    public AnimatorTrophies GetTrophies()
    {
        return null;
    }
}