package eatyourbeets.actions.orbs;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import eatyourbeets.utilities.GameActionsHelper_Legacy;
import eatyourbeets.orbs.Fire;
import eatyourbeets.powers.animator.BurningPower;
import eatyourbeets.utilities.GameUtilities;

public class FireOrbPassiveAction extends AbstractGameAction
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
        for (AbstractMonster m : GameUtilities.GetCurrentEnemies(true))
        {
            if (m.currentHealth > maxHealth)
            {
                maxHealth = m.currentHealth;
                enemy = m;
            }
        }

        if (enemy != null)
        {
            GameActionsHelper_Legacy.SetOrder(GameActionsHelper_Legacy.Order.Top);

            GameActionsHelper_Legacy.ApplyPower(p, enemy, new BurningPower(enemy, p, Fire.BURNING_AMOUNT), Fire.BURNING_AMOUNT);

            int actualDamage = AbstractOrb.applyLockOn(enemy, fire.passiveAmount);
            if (actualDamage > 0)
            {
                GameActionsHelper_Legacy.DamageTarget(p, enemy, actualDamage, DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.FIRE);
            }

            GameActionsHelper_Legacy.ResetOrder();
        }

        this.isDone = true;
    }
}