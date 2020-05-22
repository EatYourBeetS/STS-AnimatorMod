package eatyourbeets.actions.special;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.actions.EYBAction;
import eatyourbeets.interfaces.delegates.FuncT1;
import eatyourbeets.utilities.GameActions;

import java.util.ArrayList;

public class DelayAllActions extends EYBAction
{
    protected final ArrayList<AbstractGameAction> actions = new ArrayList<>();
    protected FuncT1<Boolean, AbstractGameAction> except;

    public DelayAllActions()
    {
        super(ActionType.SPECIAL);
    }

    public DelayAllActions Except(FuncT1<Boolean, AbstractGameAction> except)
    {
        this.except = except;

        return this;
    }

    @Override
    protected void FirstUpdate()
    {
        for (AbstractGameAction action : AbstractDungeon.actionManager.actions)
        {
            if (action != this && (except == null || !except.Invoke(action)))
            {
                actions.add(action);
            }
        }

        AbstractDungeon.actionManager.actions.removeAll(actions);
        if (actions.size() > 0)
        {
            GameActions.Last.Callback(() -> AbstractDungeon.actionManager.actions.addAll(actions));
        }

        Complete();
    }
}
