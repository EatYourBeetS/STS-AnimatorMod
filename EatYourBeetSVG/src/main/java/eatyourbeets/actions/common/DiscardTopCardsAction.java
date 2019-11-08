package eatyourbeets.actions.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DiscardSpecificCardAction;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import eatyourbeets.utilities.GameActionsHelper;

public class DiscardTopCardsAction extends AbstractGameAction
{
    private final CardGroup group;

    public DiscardTopCardsAction(CardGroup group, int amount)
    {
        this.amount = amount;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_MED;
        this.group = group;
    }

    public void update()
    {
        int size = group.size();
        int max = Math.min(size, amount);

        for (int i = 1; i <= max; i++)
        {
            GameActionsHelper.AddToBottom(new DiscardSpecificCardAction(group.group.get(size - i), group));
        }

        this.isDone = true;
    }
}
