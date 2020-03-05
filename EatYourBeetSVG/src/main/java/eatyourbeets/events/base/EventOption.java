package eatyourbeets.events.base;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.status.Wound;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import eatyourbeets.cards.base.FakeAbstractCard;
import eatyourbeets.interfaces.delegates.ActionT0;
import eatyourbeets.interfaces.delegates.ActionT1;
import eatyourbeets.interfaces.delegates.ActionT2;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.GenericCallback;

import java.util.ArrayList;

public class EventOption
{
    protected final ArrayList<GenericCallback<EventOption>> callbacks = new ArrayList<>();
    protected final FakeAbstractCard sampleCard = new FakeAbstractCard(new Wound());
    protected String text;
    protected int index;

    public AbstractRelic relic;
    public AbstractCard card;
    public boolean disabled;

    public EventOption(int id, String text)
    {
        this.index = id;
        this.text = text;
    }

    public void OnSelect()
    {
        for (GenericCallback<EventOption> callback : callbacks)
        {
            callback.Complete(this);
        }
    }

    public EventOption SetDisabled(boolean disabled)
    {
        this.disabled = disabled;

        return this;
    }

    public EventOption SetCard(AbstractCard card)
    {
        this.card = card;

        GameUtilities.CopyVisualProperties(card, sampleCard);

        return this;
    }

    public EventOption SetRelic(AbstractRelic relic)
    {
        this.relic = relic;

        return this;
    }

    public EventOption AddCallback(Object state, ActionT2<Object, EventOption> onCompletion)
    {
        callbacks.add(GenericCallback.FromT2(onCompletion, state));

        return this;
    }

    public EventOption AddCallback(ActionT1<EventOption> onCompletion)
    {
        callbacks.add(GenericCallback.FromT1(onCompletion));

        return this;
    }

    public EventOption AddCallback(ActionT0 onCompletion)
    {
        callbacks.add(GenericCallback.FromT0(onCompletion));

        return this;
    }
}
