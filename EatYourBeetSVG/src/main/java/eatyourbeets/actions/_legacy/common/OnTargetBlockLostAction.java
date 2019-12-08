package eatyourbeets.actions._legacy.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class OnTargetBlockLostAction extends AbstractGameAction
{
    private final DamageAction damageAction;
    private final AbstractGameAction action;
    private int initialBlock;

    public OnTargetBlockLostAction(AbstractCreature target, DamageAction damageAction, AbstractGameAction action)
    {
        this.target = target;
        this.damageAction = damageAction;
        this.action = action;
        this.duration = Settings.ACTION_DUR_FAST;
        this.actionType = damageAction.actionType;
    }

    public void update()
    {
        if (this.duration == Settings.ACTION_DUR_FAST)
        {
            this.initialBlock = target.currentBlock;
            this.tickDuration();
        }

        if (!this.damageAction.isDone)
        {
            this.damageAction.update();

            if (this.damageAction.isDone && action != null)
            {
                if (initialBlock > 0 && target.currentBlock < initialBlock)
                {
                    action.amount = initialBlock - target.currentBlock;
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
