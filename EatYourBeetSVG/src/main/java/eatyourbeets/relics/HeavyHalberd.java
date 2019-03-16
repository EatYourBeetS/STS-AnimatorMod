package eatyourbeets.relics;

import eatyourbeets.powers.PlayerStatistics;

public class HeavyHalberd extends AnimatorRelic
{
    public static final String ID = CreateFullID(HeavyHalberd.class.getSimpleName());

    public HeavyHalberd()
    {
        super(ID, RelicTier.UNCOMMON, LandingSound.HEAVY);
    }

    @Override
    public void atTurnStartPostDraw()
    {
        super.atTurnStartPostDraw();

        PlayerStatistics.AddTurnDamageMultiplier(15);
        this.flash();
    }
}