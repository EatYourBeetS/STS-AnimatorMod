package eatyourbeets.ui.common;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
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
    public State state;

    protected GenericCallback<ControllableCard> onUpdate;
    protected GenericCallback<ControllableCard> onSelect;

    public ControllableCard(AbstractCard card)
    {
        this.card = card;
        this.state = State.Enabled;
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

    public void Update(ControllableCardPile pile)
    {
        card.update();

        if (card.hb.hovered)
        {
            pile.RefreshTimer();
        }
    }

    public void Render(SpriteBatch sb, ControllableCardPile pile)
    {
        card.render(sb);
    }
}
