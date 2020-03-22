package eatyourbeets.actions.orbs;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import eatyourbeets.actions.EYBAction;
import eatyourbeets.effects.vfx.FireballEffect;
import eatyourbeets.orbs.animator.Fire;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class FireOrbPassiveAction extends EYBAction
{
    protected final Fire orb;

    public FireOrbPassiveAction(Fire orb, int damage)
    {
        super(ActionType.DEBUFF);

        this.orb = orb;

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
                GameActions.Top.DealDamage(source, enemy, actualDamage, DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.FIRE)
                .SetVFX(true, true);
            }

            GameActions.Top.Wait(0.15f);
            GameActions.Top.VFX(new FireballEffect(orb.hb.cX, orb.hb.cY, enemy.hb.cX, enemy.hb.cY).SetColor(Color.RED, Color.ORANGE));
        }

        Complete();
    }
}