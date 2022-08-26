package eatyourbeets.actions.animator;

import basemod.BaseMod;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import eatyourbeets.actions.EYBActionWithCallback;
import eatyourbeets.cards.animator.special.ThrowingKnife;
import eatyourbeets.interfaces.delegates.ActionT0;
import eatyourbeets.interfaces.delegates.ActionT1;
import eatyourbeets.interfaces.delegates.FuncT0;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;

public class CreateThrowingKnives extends EYBActionWithCallback<AbstractCard>
{
    protected boolean upgraded;

    public CreateThrowingKnives(int amount)
    {
        super(ActionType.CARD_MANIPULATION);

        Initialize(amount);
    }

    public CreateThrowingKnives SetUpgrade(boolean upgraded)
    {
        this.upgraded = upgraded;

        return this;
    }

    @Override
    protected void FirstUpdate()
    {
        final int max = Math.min(amount, BaseMod.MAX_HAND_SIZE - player.hand.size());
        final FuncT0<AbstractCard> cardFunc = GR.Animator.IsSelected() ? ThrowingKnife::GetRandomCardInBattle : eatyourbeets.cards.animatorClassic.special.ThrowingKnife::GetRandomCardInBattle;
        for (int i = 0; i < max; i++)
        {
            GameActions.Top.MakeCard(cardFunc.Invoke(), player.hand)
            .SetUpgrade(upgraded, false).CancelIfFull(true)
            .AddCallback((ActionT1<AbstractCard>) this::Complete)
            .SetDuration(Settings.ACTION_DUR_FASTER, false);
        }

        Complete();
    }


}
