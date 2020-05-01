package eatyourbeets.interfaces.subscribers;

import com.megacrit.cardcrawl.stances.AbstractStance;

public interface OnStanceChangedSubscriber {
    void OnStanceChanged(AbstractStance oldStance, AbstractStance newStance);
}