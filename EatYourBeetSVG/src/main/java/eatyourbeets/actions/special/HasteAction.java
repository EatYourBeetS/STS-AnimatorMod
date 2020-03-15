package eatyourbeets.actions.special;

import eatyourbeets.actions.EYBAction;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.utilities.GameActions;

public class HasteAction extends EYBAction
{
    public HasteAction(EYBCard card)
    {
        super(ActionType.SPECIAL);

        this.isRealtime = true;
        this.card = card;

        if (!card.haste)
        {
            isDone = true;
        }

        Initialize(1);
    }

    @Override
    protected void FirstUpdate()
    {
        GameActions.Top.Draw(1);
        GameActions.Top.Flash(card);
    }

    @Override
    protected void UpdateInternal()
    {
        super.UpdateInternal();

        if (isDone)
        {
            ((EYBCard)card).haste = false;
        }
    }
}
