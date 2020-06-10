package eatyourbeets.relics.animator;

import com.megacrit.cardcrawl.powers.VulnerablePower;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

public class HeavyHalberd extends AnimatorRelic
{
    public static final String ID = CreateFullID(HeavyHalberd.class);
    public static final int FORCE_AMOUNT = 1;

    public HeavyHalberd()
    {
        super(ID, RelicTier.RARE, LandingSound.HEAVY);
    }

    @Override
    public String getUpdatedDescription()
    {
        return FormatDescription(FORCE_AMOUNT);
    }

    @Override
    public void atTurnStartPostDraw()
    {
        super.atTurnStartPostDraw();

        int force = JUtils.Count(GameUtilities.GetEnemies(true), m -> m.hasPower(VulnerablePower.POWER_ID)) * FORCE_AMOUNT;
        if (force > 0)
        {
            GameActions.Bottom.GainForce(force);
            flash();
        }
    }
}