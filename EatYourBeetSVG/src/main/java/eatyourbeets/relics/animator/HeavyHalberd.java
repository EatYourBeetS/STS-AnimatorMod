package eatyourbeets.relics.animator;

import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.relics.AnimatorRelic;

public class HeavyHalberd extends AnimatorRelic
{
    public static final String ID = CreateFullID(HeavyHalberd.class.getSimpleName());

    private static final int DAMAGE_MULTIPLIER = 15;

    public HeavyHalberd()
    {
        super(ID, RelicTier.UNCOMMON, LandingSound.HEAVY);
    }

    @Override
    public String getUpdatedDescription()
    {
        return DESCRIPTIONS[0] + DAMAGE_MULTIPLIER + DESCRIPTIONS[1];
    }

    @Override
    public void atTurnStartPostDraw()
    {
        super.atTurnStartPostDraw();

        PlayerStatistics.AddTurnDamageMultiplier(DAMAGE_MULTIPLIER);
        this.flash();
    }
}