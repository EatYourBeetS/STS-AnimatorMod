package eatyourbeets.actions.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;

import java.util.UUID;

public class ModifyCostAction extends AbstractGameAction
{
    final UUID uuid;

    public ModifyCostAction(UUID targetUUID, int amount)
    {
        setValues(this.target, this.source, amount);
        this.actionType = ActionType.CARD_MANIPULATION;
        this.uuid = targetUUID;
    }

    public void update()
    {
        for (AbstractCard c : com.megacrit.cardcrawl.helpers.GetAllInBattleInstances.get(this.uuid))
        {
            c.costForTurn = amount;
            c.cost = amount;
            c.isCostModified = false;
        }

        this.isDone = true;
    }
}

