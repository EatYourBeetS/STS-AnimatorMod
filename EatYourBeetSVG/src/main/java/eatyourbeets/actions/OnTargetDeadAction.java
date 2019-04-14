package eatyourbeets.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.MinionPower;
import com.megacrit.cardcrawl.powers.RegrowPower;

public class OnTargetDeadAction extends AnimatorAction
{
    private final AbstractGameAction damageAction;
    private final AbstractGameAction action;
    private final boolean includeMinions;

    public OnTargetDeadAction(AbstractCreature target, AbstractGameAction damageAction, AbstractGameAction action)
    {
        this(target, damageAction, action, false);
    }

    public OnTargetDeadAction(AbstractCreature target, AbstractGameAction damageAction, AbstractGameAction action, boolean includeMinions)
    {
        this.includeMinions = includeMinions;
        this.target = target;
        this.action = action;
        this.duration = Settings.ACTION_DUR_FAST;
        this.actionType = ActionType.DAMAGE;
        this.damageAction = damageAction;
    }

    public void update()
    {
        if (updateDamageAction())
        {
            AbstractMonster monster = ((AbstractMonster)this.target);
            if ((monster.isDying || monster.currentHealth <= 0) && !monster.halfDead &&
                 (includeMinions || !monster.hasPower(MinionPower.POWER_ID) || !monster.hasPower(RegrowPower.POWER_ID)))
            {
                AbstractDungeon.actionManager.addToTop(action);
            }

            this.isDone = true;
        }
    }

    private boolean updateDamageAction()
    {
        if (damageAction == null)
        {
            return true;
        }
        else if (!damageAction.isDone)
        {
            damageAction.update();
        }

        return damageAction.isDone;
    }
}
