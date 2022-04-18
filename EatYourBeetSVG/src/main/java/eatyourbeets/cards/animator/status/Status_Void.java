package eatyourbeets.cards.animator.status;

import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.utilities.GameActions;

public class Status_Void extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Status_Void.class)
            .SetStatus(UNPLAYABLE_COST, CardRarity.RARE, EYBCardTarget.None);

    public Status_Void()
    {
        this(false);
    }

    public Status_Void(boolean upgrade)
    {
        super(DATA);

        Initialize(0, 0);

        SetEndOfTurnPlay(false);
        SetEthereal(true);

        if (upgrade)
        {
            upgrade();
        }
    }

    @Override
    protected void OnUpgrade()
    {
        SetEthereal(false);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        GameActions.Bottom.SpendEnergy(1, false);
        GameActions.Bottom.Flash(this);
    }
}