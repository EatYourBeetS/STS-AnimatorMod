package eatyourbeets.characters.Loadouts;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.helpers.Hitbox;
import eatyourbeets.utilities.JavaUtilities;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.cards.base.Synergy;
import eatyourbeets.characters.AnimatorCustomLoadout;
import eatyourbeets.characters.AnimatorTrophies;

import java.util.ArrayList;

public class Random extends AnimatorCustomLoadout
{
    public Random()
    {
        Synergy s = Synergies.ANY;

        this.ID = s.ID;
        this.Name = "Random";
        this.StartingGold = 0;
        this.MaxHP = 0;
    }

    @Override
    public ArrayList<String> GetStartingDeck()
    {
        return new ArrayList<>();
    }

    @Override
    protected String GetTrophyMessage(int trophy)
    {
        return null;
    }

    public void OnVictory(AnimatorCustomLoadout currentLoadout, int ascensionLevel)
    {
        JavaUtilities.Logger.error("The random Loadout cannot reward trophies!");
    }

    @Override
    public void RenderTrophies(Hitbox trophy1Hb, Hitbox trophy2Hb, Hitbox trophy3Hb, SpriteBatch sb)
    {

    }

    @Override
    public void UpdateTrophies(Hitbox trophy1Hb, Hitbox trophy2Hb, Hitbox trophy3Hb)
    {

    }

    @Override
    public AnimatorTrophies GetTrophies(boolean flush)
    {
        return null;
    }
}