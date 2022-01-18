package pinacolada.relics.pcl;

import com.megacrit.cardcrawl.powers.RegenPower;
import pinacolada.relics.PCLRelic;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

public class HallowedScabbard extends PCLRelic
{
    public static final String ID = CreateFullID(HallowedScabbard.class);
    public static final int DAMAGE_THRESHOLD = 12;
    public static final int REGENERATION = 4;
    public static final int BLESSING = 2;

    public HallowedScabbard()
    {
        super(ID, RelicTier.RARE, LandingSound.MAGICAL);
    }

    @Override
    public String getUpdatedDescription()
    {
        return PCLJUtils.Format(DESCRIPTIONS[0], DAMAGE_THRESHOLD, REGENERATION, BLESSING);
    }

    @Override
    public void atBattleStart()
    {
        super.atBattleStart();

        SetEnabled(true);
        SetCounter(0);
    }

    @Override
    public void onVictory()
    {
        super.onVictory();

        SetEnabled(true);
        SetCounter(-1);
    }

    @Override
    public void onLoseHp(int damageAmount)
    {
        super.onLoseHp(damageAmount);

        if (PCLGameUtilities.InBattle())
        {
            if (AddCounter(damageAmount) >= DAMAGE_THRESHOLD && IsEnabled())
            {
                PCLActions.Bottom.GainInvocation(BLESSING, false);
                PCLActions.Bottom.StackPower(new RegenPower(player, REGENERATION));
                SetEnabled(false);
                flash();
            }
        }
    }
}