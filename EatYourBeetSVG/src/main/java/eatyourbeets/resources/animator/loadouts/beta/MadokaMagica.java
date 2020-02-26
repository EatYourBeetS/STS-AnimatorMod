package eatyourbeets.resources.animator.loadouts.beta;

import eatyourbeets.cards.animator.beta.MadokaMagica.Kyubey;
import eatyourbeets.cards.base.AnimatorCard_UltraRare;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.resources.animator.misc.AnimatorLoadout;

import java.util.ArrayList;

public class MadokaMagica extends AnimatorLoadout
{
    // Add this loadout in AnimatorPlayerData.AddBetaLoadouts();
    public MadokaMagica()
    {
        super(Synergies.MadokaMagica);

        IsBeta = true;
    }

    @Override
    public ArrayList<String> GetStartingDeck()
    {
        return new ArrayList<>();
    }

    @Override
    public String GetSymbolicCardID()
    {
        return Kyubey.DATA.ID;
    }

    @Override
    public AnimatorCard_UltraRare GetUltraRare()
    {
        return null;
    }
}
