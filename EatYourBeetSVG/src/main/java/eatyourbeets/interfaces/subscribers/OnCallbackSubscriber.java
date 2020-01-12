package eatyourbeets.interfaces.subscribers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;

public interface OnCallbackSubscriber
{
    void OnCallback(Object state, AbstractGameAction action);
}
