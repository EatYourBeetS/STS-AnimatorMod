package eatyourbeets.actions.basic;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import eatyourbeets.actions.EYBAction;
import eatyourbeets.utilities.GameUtilities;

public class RemoveBlock extends EYBAction
{
    protected boolean skipAnimation;
    protected boolean instant;

    public RemoveBlock(AbstractCreature target, AbstractCreature source)
    {
        this(target, source, -1);
    }

    public RemoveBlock(AbstractCreature target, AbstractCreature source, int amount)
    {
        super(ActionType.BLOCK, Settings.ACTION_DUR_FAST);

        Initialize(source, target, amount);
    }

    public RemoveBlock SetVFX(boolean instant, boolean skipAnimation)
    {
        this.instant = instant;
        this.skipAnimation = skipAnimation;

        return this;
    }

    @Override
    protected void FirstUpdate()
    {
        if (target != null && !GameUtilities.IsDeadOrEscaped(target) && target.currentBlock > 0)
        {
            if (amount > 0)
            {
                this.target.loseBlock(amount, skipAnimation);
            }
            else
            {
                this.target.loseBlock(skipAnimation);
            }
        }

        if (instant)
        {
            Complete();
        }
    }
}
