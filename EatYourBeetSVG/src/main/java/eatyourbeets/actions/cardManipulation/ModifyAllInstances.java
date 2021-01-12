package eatyourbeets.actions.cardManipulation;

import com.megacrit.cardcrawl.cards.AbstractCard;
import eatyourbeets.actions.EYBActionWithCallback;
import eatyourbeets.interfaces.delegates.ActionT1;
import eatyourbeets.interfaces.delegates.ActionT2;
import eatyourbeets.utilities.GameUtilities;

import java.util.UUID;

public class ModifyAllInstances extends EYBActionWithCallback<AbstractCard>
{
    protected final UUID uuid;
    protected boolean includeMasterDeck;

    public ModifyAllInstances(UUID targetUUID, ActionT1<AbstractCard> onCompletion)
    {
        this(targetUUID);

        AddCallback(onCompletion);
    }

    public <S> ModifyAllInstances(UUID targetUUID, S state, ActionT2<S, AbstractCard> onCompletion)
    {
        this(targetUUID);

        AddCallback(state, onCompletion);
    }

    public ModifyAllInstances(UUID targetUUID)
    {
        super(ActionType.CARD_MANIPULATION);

        this.uuid = targetUUID;

        Initialize(1);
    }

    public ModifyAllInstances IncludeMasterDeck(boolean includeMasterDeck)
    {
        this.includeMasterDeck = includeMasterDeck;

        return this;
    }

    @Override
    protected void FirstUpdate()
    {
        if (includeMasterDeck)
        {
            for (AbstractCard card : GameUtilities.GetAllInstances(uuid))
            {
                Complete(card);
            }
        }
        else
        {
            for (AbstractCard card : GameUtilities.GetAllInBattleInstances(uuid))
            {
                Complete(card);
            }
        }
    }
}
