package eatyourbeets.actions.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.helpers.GetAllInBattleInstances;

import java.util.UUID;

public class ModifyBlockActionWhichActuallyWorks extends AbstractGameAction
{
    private UUID uuid;

    public ModifyBlockActionWhichActuallyWorks(UUID targetUUID, int amount)
    {
        this.setValues(this.target, this.source, amount);
        this.actionType = AbstractGameAction.ActionType.CARD_MANIPULATION;
        this.uuid = targetUUID;
    }

    public void update()
    {

        for (AbstractCard c : GetAllInBattleInstances.get(this.uuid))
        {
            c.baseBlock += this.amount;
            if (c.baseBlock < 0)
            {
                c.baseBlock = 0;
            }
        }

        this.isDone = true;
    }
}
