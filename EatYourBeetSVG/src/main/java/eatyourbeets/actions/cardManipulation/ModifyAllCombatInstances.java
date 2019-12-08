package eatyourbeets.actions.cardManipulation;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.helpers.GetAllInBattleInstances;
import eatyourbeets.actions.EYBActionWithCallback;

import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ModifyAllCombatInstances extends EYBActionWithCallback<AbstractCard>
{
    private final UUID uuid;

    public ModifyAllCombatInstances(UUID targetUUID, Consumer<AbstractCard> onCompletion)
    {
        this(targetUUID);

        AddCallback(onCompletion);
    }

    public ModifyAllCombatInstances(UUID targetUUID, Object state, BiConsumer<Object, AbstractCard> onCompletion)
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
