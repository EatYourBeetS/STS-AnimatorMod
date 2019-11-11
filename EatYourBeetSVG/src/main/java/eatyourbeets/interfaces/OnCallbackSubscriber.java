package eatyourbeets.interfaces;

import com.megacrit.cardcrawl.actions.AbstractGameAction;

public interface OnCallbackSubscriber
{
    void OnCallback(Object state, AbstractGameAction action);
}
