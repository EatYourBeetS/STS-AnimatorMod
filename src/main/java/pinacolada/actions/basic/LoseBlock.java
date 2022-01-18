package pinacolada.actions.basic;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import eatyourbeets.actions.EYBActionWithCallback;

public class LoseBlock extends EYBActionWithCallback<AbstractCreature>
{
    protected boolean canLoseLess;
    protected boolean skipAnimation;

    public LoseBlock(AbstractCreature target, AbstractCreature source, int amount)
    {
        super(ActionType.BLOCK, Settings.ACTION_DUR_XFAST);

        Initialize(source, target, amount);

        if (amount <= 0)
        {
            Complete();
        }
    }

    public LoseBlock SkipAnimation(boolean skipAnimation)
    {
        this.skipAnimation = skipAnimation;

        return this;
    }

    public LoseBlock CanLoseLess(boolean canLoseLess)
    {
        this.canLoseLess = canLoseLess;

        return this;
    }

    @Override
    protected void FirstUpdate()
    {
        if (!target.isDying && !target.isDead && amount > 0)
        {
            if (target.currentBlock < amount && canLoseLess)
            {
                Complete();
                return;
            }

            target.loseBlock(amount, skipAnimation);
            player.hand.applyPowers();
        }
    }

    @Override
    protected void UpdateInternal(float deltaTime)
    {
        if (TickDuration(deltaTime))
        {
            Complete(target);
        }
    }
}
