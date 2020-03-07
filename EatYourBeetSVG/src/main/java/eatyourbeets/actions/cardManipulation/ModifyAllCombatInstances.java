package eatyourbeets.actions.cardManipulation;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.helpers.GetAllInBattleInstances;
import eatyourbeets.actions.EYBActionWithCallback;
import eatyourbeets.interfaces.delegates.ActionT1;
import eatyourbeets.interfaces.delegates.ActionT2;

import java.util.UUID;

public class ModifyAllCombatInstances extends EYBActionWithCallback<AbstractCard>
{
    private final UUID uuid;

    public ModifyAllCombatInstances(UUID targetUUID, ActionT1<AbstractCard> onCompletion)
    {
        this(targetUUID);

        AddCallback(onCompletion);
    }

    public ModifyAllCombatInstances(UUID targetUUID, Object state, ActionT2<Object, AbstractCard> onCompletion)
    {
        this(targetUUID);

        AddCallback(state, onCompletion);
    }

    public ModifyAllCombatInstances(UUID targetUUID)
    {
        super(ActionType.CARD_MANIPULATION);

        this.uuid = targetUUID;

        Initialize(1);
    }

    @Override
    protected void FirstUpdate()
    {
        for (AbstractCard card : GetAllInBattleInstances.get(uuid))
        {
            Complete(card);
        }
    }
}
