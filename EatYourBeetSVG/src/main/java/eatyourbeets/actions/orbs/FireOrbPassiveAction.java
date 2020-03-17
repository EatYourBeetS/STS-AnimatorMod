package eatyourbeets.actions.orbs;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import eatyourbeets.actions.EYBAction;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.orbs.animator.Fire;
import eatyourbeets.utilities.GameUtilities;

public class FireOrbPassiveAction extends EYBAction
{
    public FireOrbPassiveAction(int damage)
    {
        super(ActionType.DEBUFF);

        Initialize(damage);
    }

    @Override
    protected void FirstUpdate()
    {
        int maxHealth = Integer.MIN_VALUE;
        AbstractMonster enemy = null;

        for (AbstractMonster m : GameUtilities.GetAllEnemies(true))
        {
            if (m.currentHealth > maxHealth)
            {
                maxHealth = m.currentHealth;
                enemy = m;
            }
        }

        if (enemy != null)
        {
            GameActions.Top.ApplyBurning(source, enemy, Fire.BURNING_AMOUNT);

            int actualDamage = AbstractOrb.applyLockOn(enemy, amount);
            if (actualDamage > 0)
            {
                GameActions.Top.DealDamage(source, enemy, actualDamage, DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.FIRE);
            }
        }

        Complete();
    }
}