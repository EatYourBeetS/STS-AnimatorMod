package eatyourbeets.relics.animatorClassic;

import com.megacrit.cardcrawl.powers.VulnerablePower;
import eatyourbeets.relics.AnimatorClassicRelic;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

public class HeavyHalberd extends AnimatorClassicRelic
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
        return FormatDescription(0, FORCE_AMOUNT);
    }

    @Override
    public void atTurnStartPostDraw()
    {
        super.atTurnStartPostDraw();

        int force = JUtils.Count(GameUtilities.GetAllCharacters(true), c -> c.hasPower(VulnerablePower.POWER_ID)) * FORCE_AMOUNT;
        if (force > 0)
        {
            GameActions.Bottom.GainForce(force);
            flash();
        }
    }
}