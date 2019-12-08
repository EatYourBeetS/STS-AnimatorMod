package eatyourbeets.actions._legacy.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;

import java.util.UUID;

public class ModifyCostForTurnAction extends AbstractGameAction
{
    final UUID uuid;

    public ModifyCostForTurnAction(UUID targetUUID, int amount)
    {
        setValues(this.target, this.source, amount);
        this.actionType = ActionType.CARD_MANIPULATION;
        this.uuid = targetUUID;
    }

    public void update()
    {
        for (AbstractCard c : com.megacrit.cardcrawl.helpers.GetAllInBattleInstances.get(this.uuid))
        {
            c.setCostForTurn(c.costForTurn + this.amount);
            //c.modifyCostForTurn(this.amount);
        }

        this.isDone = true;
    }
}

