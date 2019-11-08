package eatyourbeets.relics.animator;

import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.utilities.Utilities;

public class HeavyHalberd extends AnimatorRelic
{
    public static final String ID = CreateFullID(HeavyHalberd.class.getSimpleName());

    private static final int FORCE_AMOUNT = 1;

    public HeavyHalberd()
    {
        super(ID, RelicTier.RARE, LandingSound.HEAVY);
    }

    @Override
    public String getUpdatedDescription()
    {
        return Utilities.Format(DESCRIPTIONS[0], FORCE_AMOUNT);
    }

    @Override
    public void atTurnStartPostDraw()
    {
        super.atTurnStartPostDraw();

        int force = 0;
        for (AbstractMonster m : PlayerStatistics.GetCurrentEnemies(true))
        {
            if (m.hasPower(VulnerablePower.POWER_ID))
            {
                force += FORCE_AMOUNT;
            }
        }

        if (force > 0)
        {
            GameActionsHelper.GainForce(force);
            this.flash();
        }
    }
}