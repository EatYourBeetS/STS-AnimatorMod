package pinacolada.powers.replacement;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.LockOnPower;
import pinacolada.powers.PCLCombatStats;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLJUtils;

public class PCLLockOnPower extends LockOnPower implements CloneablePowerInterface
{
    protected static final String FAKE_POWER_ID = GR.PCL.CreateID(LockOnPower.class.getSimpleName());

    protected static final PowerStrings fakePowerStrings;
    public static final int ATTACK_MULTIPLIER = 25;
    public static final int ORB_MULTIPLIER = 50;

    static {
        fakePowerStrings = CardCrawlGame.languagePack.getPowerStrings(FAKE_POWER_ID);
    }

    public static int GetOrbMultiplier()
    {
        return (ORB_MULTIPLIER + PCLCombatStats.GetPlayerEffectBonus(POWER_ID));
    }

    public static int GetAttackMultiplier()
    {
        return (ATTACK_MULTIPLIER + PCLCombatStats.GetPlayerEffectBonus(POWER_ID));
    }


    public PCLLockOnPower(AbstractCreature owner, int amount)
    {
        super(owner,amount);
    }

    @Override
    public void updateDescription() {
        this.description = PCLJUtils.Format(fakePowerStrings.DESCRIPTIONS[0],GetOrbMultiplier(),GetAttackMultiplier(),amount,amount == 1 ? fakePowerStrings.DESCRIPTIONS[1] : fakePowerStrings.DESCRIPTIONS[2]);
    }

    @Override
    public AbstractPower makeCopy()
    {
        return new PCLLockOnPower(owner, amount);
    }
}
