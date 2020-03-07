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

public class EYBEventOption
{
    protected final ArrayList<GenericCallback<EYBEventOption>> callbacks = new ArrayList<>();
    protected final FakeAbstractCard sampleCard = new FakeAbstractCard(new Wound());
    protected String text;

    public AbstractRelic relic;
    public AbstractCard card;
    public boolean disabled;

    public EYBEventOption(String text)
    {
        this.text = text;
    }

    public void OnSelect()
    {
        for (GenericCallback<EYBEventOption> callback : callbacks)
        {
            callback.Complete(this);
        }
    }

    public EYBEventOption SetDisabled(boolean disabled)
    {
        this.disabled = disabled;

        return this;
    }

    public EYBEventOption SetCard(AbstractCard card)
    {
        this.card = card;

        GameUtilities.CopyVisualProperties(card, sampleCard);

        return this;
    }

    public EYBEventOption SetRelic(AbstractRelic relic)
    {
        this.relic = relic;

        return this;
    }

    public EYBEventOption AddCallback(Object state, ActionT2<Object, EYBEventOption> onCompletion)
    {
        callbacks.add(GenericCallback.FromT2(onCompletion, state));

        return this;
    }

    public EYBEventOption AddCallback(ActionT1<EYBEventOption> onCompletion)
    {
        callbacks.add(GenericCallback.FromT1(onCompletion));

        return this;
    }

    public EYBEventOption AddCallback(ActionT0 onCompletion)
    {
        callbacks.add(GenericCallback.FromT0(onCompletion));

        return this;
    }
}
