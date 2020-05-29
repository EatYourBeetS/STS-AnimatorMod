package eatyourbeets.ui.common;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import eatyourbeets.interfaces.delegates.ActionT1;
import eatyourbeets.interfaces.delegates.ActionT2;
import eatyourbeets.utilities.GenericCallback;

public class ControllableCard
{
    public enum State
    {
        Deleted,
        Enabled,
        Disabled
    }

    public final AbstractCard card;
    public CardGroup originGroup;
    public State state;

    protected GenericCallback<ControllableCard> onUpdate;
    protected GenericCallback<ControllableCard> onSelect;

    public ControllableCard(AbstractCard card, CardGroup group)
    {
        //We save a statequivalentcopy of the card and set the uuids to be equal so we can find one based on the other
        //We save a copy instead of the original so we don't render one card in two different locations and cause visual glitch
        this.card = card.makeStatEquivalentCopy();
        this.card.uuid = card.uuid;
        this.state = State.Enabled;
        this.originGroup = group;
    }

    public ControllableCard OnUpdate(Object state, ActionT2<Object, ControllableCard> onCompletion)
    {
        onUpdate = GenericCallback.FromT2(onCompletion, state);

        return this;
    }

    public ControllableCard OnUpdate(ActionT1<ControllableCard> onCompletion)
    {
        onUpdate = GenericCallback.FromT1(onCompletion);

        return this;
    }

    public ControllableCard OnSelect(Object state, ActionT2<Object, ControllableCard> onCompletion)
    {
        onSelect = GenericCallback.FromT2(onCompletion, state);

        return this;
    }

    public ControllableCard OnSelect(ActionT1<ControllableCard> onCompletion)
    {
        onSelect = GenericCallback.FromT1(onCompletion);

        return this;
    }

    public boolean IsEnabled()
    {
        return state == State.Enabled;
    }

    public boolean IsDeleted()
    {
        return state == State.Deleted;
    }

    public void SetEnabled(boolean enabled)
    {
        if (!IsDeleted())
        {
            state = (enabled ? State.Enabled : State.Disabled);
        }
    }

    public void Delete()
    {
        state = State.Deleted;
    }

    public void Update()
    {
        if (onUpdate != null)
        {
            onUpdate.Complete(this);
        }
    }

    public void Select()
    {
        if (onSelect != null)
        {
            onSelect.Complete(this);
        }
    }
}
