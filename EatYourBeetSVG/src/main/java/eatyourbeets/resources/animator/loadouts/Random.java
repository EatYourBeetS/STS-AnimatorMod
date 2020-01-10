package eatyourbeets.resources.animator.loadouts;

import eatyourbeets.cards.base.AnimatorCard_UltraRare;
import eatyourbeets.resources.animator.metrics.AnimatorLoadout;
import eatyourbeets.resources.animator.metrics.AnimatorTrophies;
import eatyourbeets.ui.animator.characterSelect.AnimatorLoadoutRenderer;

import java.util.ArrayList;

public class Random extends AnimatorLoadout
{
    public Random()
    {
        super("Random", 0);

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
    public String GetRepresentativeCard()
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