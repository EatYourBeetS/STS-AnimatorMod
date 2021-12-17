package pinacolada.actions.special;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.actions.EYBAction;
import eatyourbeets.interfaces.delegates.FuncT1;
import pinacolada.utilities.PCLActions;

import java.util.ArrayList;

public class DelayAllActions extends EYBAction
{
    protected final ArrayList<AbstractGameAction> actions = new ArrayList<>();
    protected FuncT1<Boolean, AbstractGameAction> except;
    protected boolean currentOnly = false;

    public DelayAllActions()
    {
        this(false);
    }

    public DelayAllActions(boolean currentOnly)
    {
        super(ActionType.SPECIAL, 0.01f);

        if (currentOnly)
        {
            CreateList();
        }
    }

    public DelayAllActions Except(FuncT1<Boolean, AbstractGameAction> except)
    {
        this.except = except;

        return this;
    }

    @Override
    protected void FirstUpdate()
    {
        if (!currentOnly)
        {
            CreateList();
        }

        AbstractDungeon.actionManager.actions.removeAll(actions);
        if (actions.size() > 0)
        {
            PCLActions.Last.Callback(() -> AbstractDungeon.actionManager.actions.addAll(actions));
        }

        Complete();
    }

    protected void CreateList()
    {
        for (AbstractGameAction action : AbstractDungeon.actionManager.actions)
        {
            if (action != this && (except == null || !except.Invoke(action)))
            {
                actions.add(action);
            }
        }
    }
}
