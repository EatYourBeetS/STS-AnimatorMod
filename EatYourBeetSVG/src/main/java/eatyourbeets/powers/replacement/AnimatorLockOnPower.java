package eatyourbeets.powers.replacement;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.LockOnPower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;
import eatyourbeets.utilities.TargetHelper;

import java.util.UUID;

public class AnimatorLockOnPower extends LockOnPower implements CloneablePowerInterface
{
    protected static final String FAKE_POWER_ID = GR.Common.CreateID(LockOnPower.class.getSimpleName());

    protected static final PowerStrings fakePowerStrings;
    private static UUID battleID;
    public static final int ATTACK_MULTIPLIER = 25;
    public static final int ORB_MULTIPLIER = 50;
    public static int ENEMY_MODIFIER = 0;

    static {
        fakePowerStrings = CardCrawlGame.languagePack.getPowerStrings(FAKE_POWER_ID);
    }

    public static void AddEnemyModifier(int multiplier)
    {
        InitializeForBattle();
        ENEMY_MODIFIER += multiplier;

        for (LockOnPower p : GameUtilities.<LockOnPower>GetPowers(TargetHelper.Enemies(), POWER_ID))
        {
            p.updateDescription();
            p.flashWithoutSound();
        }
    }

    public static int GetOrbMultiplier()
    {
        return (ORB_MULTIPLIER + ENEMY_MODIFIER);
    }

    public static int GetAttackMultiplier()
    {
        return (ATTACK_MULTIPLIER + ENEMY_MODIFIER);
    }

    private static void InitializeForBattle() {
        if (CombatStats.BattleID != battleID)
        {
            battleID = CombatStats.BattleID;
            ENEMY_MODIFIER = 0;
        }
    }


    public AnimatorLockOnPower(AbstractCreature owner, int amount)
    {
        super(owner,amount);

        InitializeForBattle();
    }

    @Override
    public void updateDescription() {
        this.description = JUtils.Format(fakePowerStrings.DESCRIPTIONS[0],GetOrbMultiplier(),GetAttackMultiplier(),amount,amount == 1 ? fakePowerStrings.DESCRIPTIONS[1] : fakePowerStrings.DESCRIPTIONS[2]);
    }

    @Override
    public AbstractPower makeCopy()
    {
        return new AnimatorLockOnPower(owner, amount);
    }
}
