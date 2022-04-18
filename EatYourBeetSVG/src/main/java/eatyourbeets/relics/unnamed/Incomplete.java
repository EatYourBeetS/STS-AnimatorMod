package eatyourbeets.relics.unnamed;

import eatyourbeets.relics.UnnamedRelic;
import eatyourbeets.utilities.GameActions;

public class Incomplete extends UnnamedRelic
{
    public static final String ID = CreateFullID(Incomplete.class);

    public Incomplete()
    {
        super(ID, RelicTier.STARTER, LandingSound.MAGICAL);
    }

    @Override
    public void atBattleStart()
    {
        super.atBattleStart();

        GameActions.Bottom.Draw(1);
        GameActions.Bottom.GainEnergy(1);
        this.flash();
    }
}