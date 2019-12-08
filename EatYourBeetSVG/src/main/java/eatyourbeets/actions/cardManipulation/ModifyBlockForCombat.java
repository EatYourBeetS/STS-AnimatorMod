package eatyourbeets.actions.cardManipulation;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.helpers.GetAllInBattleInstances;
import eatyourbeets.actions.EYBAction;

import java.util.UUID;

public class ModifyBlockForCombat extends EYBAction
{
    private final boolean add;
    private final UUID uuid;

    public ModifyBlockForCombat(UUID targetUUID, int amount, boolean add)
    {
        super(ActionType.CARD_MANIPULATION);

        this.add = add;
        this.uuid = targetUUID;

        Initialize(amount);
    }

    @Override
    protected void FirstUpdate()
    {
        for (AbstractCard c : GetAllInBattleInstances.get(this.uuid))
        {
            if (add)
            {
                c.baseBlock += this.amount;
            }
            else
            {
                c.baseBlock = this.amount;
            }

            if (c.baseBlock < 0)
            {
                c.baseBlock = 0;
            }
        }

        Complete();
    }
}
