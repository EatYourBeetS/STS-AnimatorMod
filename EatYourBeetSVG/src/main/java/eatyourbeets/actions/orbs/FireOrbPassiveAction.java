package eatyourbeets.actions.orbs;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.actions.animator.AnimatorAction;
import eatyourbeets.orbs.Fire;
import eatyourbeets.powers.animator.BurningPower;
import eatyourbeets.powers.PlayerStatistics;

public class FireOrbPassiveAction extends AnimatorAction
{
    private final Fire fire;

    public FireOrbPassiveAction(Fire fire)
    {
        this.actionType = ActionType.DAMAGE;
        this.duration = 0.1F;
        this.fire = fire;
    }

    public void update()
    {
        int maxHealth = Integer.MIN_VALUE;
        AbstractMonster enemy = null;
        AbstractPlayer p = AbstractDungeon.player;
        for (AbstractMonster m : PlayerStatistics.GetCurrentEnemies(true))
        {
            if (m.currentHealth > maxHealth)
            {
                maxHealth = m.currentHealth;
                enemy = m;
            }
        }

        if (enemy != null)
        {
            GameActionsHelper.SetOrder(GameActionsHelper.Order.Top);

            GameActionsHelper.ApplyPower(p, enemy, new BurningPower(enemy, p, Fire.BURNING_AMOUNT), Fire.BURNING_AMOUNT);

            int actualDamage = AbstractOrb.applyLockOn(enemy, fire.passiveAmount);
            if (actualDamage > 0)
            {
                GameActionsHelper.DamageTarget(p, enemy, actualDamage, DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.FIRE);
            }

            GameActionsHelper.ResetOrder();
        }

        this.isDone = true;
    }
}