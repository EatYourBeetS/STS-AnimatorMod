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
    protected GenericCallback<ControllableCard> onDelete;

    public ControllableCard(AbstractCard card)
    {
        this.card = card;
        this.state = State.Enabled;
    }

    public <S> ControllableCard OnUpdate(S state, ActionT2<S, ControllableCard> onCompletion)
    {
        onUpdate = GenericCallback.FromT2(onCompletion, state);

        return this;
    }

    public ControllableCard OnUpdate(ActionT1<ControllableCard> onCompletion)
    {
        onUpdate = GenericCallback.FromT1(onCompletion);

        return this;
    }

    public <S> ControllableCard OnSelect(S state, ActionT2<S, ControllableCard> onCompletion)
    {
        onSelect = GenericCallback.FromT2(onCompletion, state);

        return this;
    }

    public ControllableCard OnSelect(ActionT1<ControllableCard> onCompletion)
    {
        onSelect = GenericCallback.FromT1(onCompletion);

        return this;
    }

    public <S> ControllableCard OnDelete(S state, ActionT2<S, ControllableCard> onCompletion)
    {
        onDelete = GenericCallback.FromT2(onCompletion, state);

        return this;
    }

    public ControllableCard OnDelete(ActionT1<ControllableCard> onCompletion)
    {
        onDelete = GenericCallback.FromT1(onCompletion);

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

        if (onDelete != null)
        {
            onDelete.Complete(this);
        }
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
