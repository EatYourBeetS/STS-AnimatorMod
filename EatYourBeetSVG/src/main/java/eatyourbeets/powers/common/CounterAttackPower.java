package eatyourbeets.powers.common;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;
import eatyourbeets.effects.SFX;
import eatyourbeets.powers.CommonPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.TargetHelper;

public class CounterAttackPower extends CommonPower
{
    public static final String POWER_ID = CreateFullID(CounterAttackPower.class);
    public static final int VULNERABLE_AMOUNT = 1;
    public static boolean retain = false;
    public boolean canActivate = false;

    public CounterAttackPower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);

        Initialize(amount, PowerType.BUFF, false);
    }

    @Override
    public void updateDescription()
    {
        this.description = FormatDescription(0, amount, VULNERABLE_AMOUNT);
    }

    @Override
    public void playApplyPowerSfx()
    {
        SFX.Play(SFX.ATTACK_IRON_1, 1.25f, 1.35f, 0.7f);
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount)
    {
        if (info.type == DamageInfo.DamageType.NORMAL && damageAmount > owner.currentBlock)
        {
            if (!retain) {
                RemovePower();
            }
        }
        else {
            canActivate = true;
        }

        return super.onAttacked(info, damageAmount);
    }

    @Override
    public void atEndOfTurn(boolean isPlayer)
    {
        super.atEndOfTurn(isPlayer);

        if (isPlayer && canActivate)
        {
            for (AbstractMonster m : GameUtilities.GetEnemies(true))
            {
                if (GameUtilities.IsAttacking(m.intent))
                {
                    return;
                }
            }

            if (!retain) {
                RemovePower();
            }
        }
    }

    @Override
    public void atStartOfTurn()
    {
        super.atStartOfTurn();

        GameActions.Bottom.StackPower(new VigorPower(owner, amount));
        GameActions.Bottom.ApplyVulnerable(TargetHelper.Enemies(), VULNERABLE_AMOUNT);
        flashWithoutSound();
        canActivate = false;
        if (!retain) {
            RemovePower();
        }

    }
}