package pinacolada.actions.orbs;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import eatyourbeets.actions.EYBAction;
import pinacolada.cards.base.PCLAttackType;
import pinacolada.effects.AttackEffects;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class DarkOrbEvokeAction extends EYBAction
{

    public DarkOrbEvokeAction(int damage)
    {
        super(ActionType.DAMAGE);

        Initialize(damage);
    }

    @Override
    protected void FirstUpdate()
    {
        int minHealth = Integer.MAX_VALUE;
        AbstractMonster enemy = null;

        for (AbstractMonster m : PCLGameUtilities.GetEnemies(true))
        {
            if (m.currentHealth < minHealth)
            {
                minHealth = m.currentHealth;
                enemy = m;
            }
        }

        if (enemy != null)
        {
            int actualDamage = AbstractOrb.applyLockOn(enemy, amount);
            if (actualDamage > 0)
            {
                PCLActions.Top.DealDamage(source, enemy, actualDamage, DamageInfo.DamageType.THORNS, AttackEffects.DARKNESS)
                        .SetVFX(true, false)
                        .SetPowerToRemove(PCLAttackType.Dark.powerToRemove, true);
            }
        }

        Complete();
    }
}
