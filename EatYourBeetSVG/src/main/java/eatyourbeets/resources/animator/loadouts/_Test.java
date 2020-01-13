package eatyourbeets.resources.animator.loadouts;

import eatyourbeets.cards.animator.colorless.uncommon.QuestionMark;
import eatyourbeets.cards.base.AnimatorCard_UltraRare;
import eatyourbeets.resources.animator.metrics.AnimatorLoadout;
import eatyourbeets.resources.animator.metrics.AnimatorTrophies;
import eatyourbeets.ui.animator.characterSelect.AnimatorLoadoutRenderer;

import java.util.ArrayList;
import java.util.HashMap;

public class _Test extends AnimatorLoadout
{
    private String cardID;

    public _Test(eatyourbeets.cards.base.Synergy synergy, String cardID, int amount)
    {
        super(synergy);

        this.cardID = cardID;
        this.IsBeta = true;
        this.ID = -1;
        this.StartingGold = 0;
        this.MaxHP = 0;
        this.libraryCards = new HashMap<>();

        for (int i = 0; i < amount; i++)
        {
            this.libraryCards.put(String.valueOf(i), new QuestionMark());
        }
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

    }

    @Override
    public AnimatorTrophies GetTrophies()
    {
        return null;
    }
}