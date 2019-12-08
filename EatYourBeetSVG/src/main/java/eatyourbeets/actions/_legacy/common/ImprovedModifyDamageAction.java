package eatyourbeets.actions._legacy.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.GetAllInBattleInstances;

import java.util.UUID;

public class ImprovedModifyDamageAction extends AbstractGameAction
{
    private final UUID uuid;

    public ImprovedModifyDamageAction(UUID targetUUID, int amount)
    {
        this.setValues(this.target, this.source, amount);
        this.actionType = ActionType.CARD_MANIPULATION;
        this.uuid = targetUUID;
    }

    public void update()
    {
        for (AbstractCard c : GetAllInBattleInstances.get(this.uuid))
        {
            c.baseDamage += this.amount;
            if (c.baseDamage < 0)
            {
                c.baseDamage = 0;
            }

            if (AbstractDungeon.player.hand.contains(c))
            {
                c.applyPowers();
            }
        }

        this.isDone = true;
    }
}

