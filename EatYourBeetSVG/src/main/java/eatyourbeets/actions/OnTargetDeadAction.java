package eatyourbeets.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class OnTargetDeadAction extends AnimatorAction
{
    private final DamageAction damageAction;
    private final AbstractGameAction action;
    private final boolean includeMinions;

    public OnTargetDeadAction(AbstractCreature target, DamageAction damageAction, AbstractGameAction action)
    {
        this(target, damageAction, action, false);
    }

    public OnTargetDeadAction(AbstractCreature target, DamageAction damageAction, AbstractGameAction action, boolean includeMinions)
    {
        this.includeMinions = includeMinions;
        this.target = target;
        this.action = action;
        this.duration = Settings.ACTION_DUR_FAST;
        this.actionType = damageAction.actionType;
        this.damageAction = damageAction;
    }

    public void update()
    {
        if (!this.damageAction.isDone)
        {
            this.damageAction.update();

            if (this.damageAction.isDone)
            {
                AbstractMonster monster = ((AbstractMonster)this.target);
                if ((monster.isDying || monster.currentHealth <= 0) && !monster.halfDead && (includeMinions || !monster.hasPower("Minion")))
                {
                    AbstractDungeon.actionManager.addToTop(action);
                }
            }
            else
            {
                return;
            }
        }

        this.isDone = true;
    }
}
