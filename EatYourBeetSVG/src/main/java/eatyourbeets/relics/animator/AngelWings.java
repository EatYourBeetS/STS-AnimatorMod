package eatyourbeets.relics.animator;

import eatyourbeets.relics.AnimatorRelic;

public class AngelWings extends AnimatorRelic
{
    public static final String ID = CreateFullID(AngelWings.class);

    public AngelWings()
    {
        super(ID, RelicTier.SPECIAL, LandingSound.MAGICAL);
    }
}