package eatyourbeets.actions._legacy.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class GuyAction extends AbstractGameAction
{
    private final AbstractCreature owner;

    public GuyAction(AbstractCreature owner, AbstractCreature target)
    {
        this.owner = owner;
        this.target = target;
        this.duration = Settings.ACTION_DUR_FAST;
        this.actionType = ActionType.BLOCK;
    }

    public void update()
    {
        if (this.duration == Settings.ACTION_DUR_FAST)
        {
            if (owner.currentBlock > 0)
            {
                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(target, target, owner.currentBlock));
            }
        }

        this.tickDuration();
    }
}
