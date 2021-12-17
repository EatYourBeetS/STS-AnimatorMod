package pinacolada.actions.special;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import eatyourbeets.actions.EYBActionWithCallback;
import eatyourbeets.interfaces.delegates.ActionT1;
import pinacolada.cards.pcl.special.ThrowingKnife;
import pinacolada.utilities.PCLActions;

public class CreateThrowingKnives extends EYBActionWithCallback<AbstractCard>
{
    protected boolean upgraded;
    protected CardGroup pile;

    public CreateThrowingKnives(int amount)
    {
        this(amount, null);
    }

    public CreateThrowingKnives(int amount, CardGroup pile)
    {
        super(ActionType.CARD_MANIPULATION);
        if (pile == null) {
            this.pile = player.hand;
        }
        else {
            this.pile = pile;
        }

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
        for (int i = 0; i < amount; i++)
        {
            PCLActions.Top.MakeCard(ThrowingKnife.GetRandomCard(), pile)
            .SetUpgrade(upgraded, false).CancelIfFull(true)
            .AddCallback((ActionT1<AbstractCard>) this::Complete)
            .SetDuration(Settings.ACTION_DUR_FASTER, false);
        }

        Complete();
    }
}
