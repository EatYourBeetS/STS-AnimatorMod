package eatyourbeets.actions.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;

import java.util.UUID;

public class ModifyMagicNumberAction extends com.megacrit.cardcrawl.actions.AbstractGameAction
{
    final UUID uuid;

    public ModifyMagicNumberAction(UUID targetUUID, int amount)
    {
        setValues(this.target, this.source, amount);
        this.actionType = AbstractGameAction.ActionType.CARD_MANIPULATION;
        this.uuid = targetUUID;
    }

    public void update()
    {
        for (AbstractCard c : com.megacrit.cardcrawl.helpers.GetAllInBattleInstances.get(this.uuid))
        {
            c.baseMagicNumber = Math.max(0, c.baseMagicNumber + this.amount);
        }
        this.isDone = true;
    }
}

