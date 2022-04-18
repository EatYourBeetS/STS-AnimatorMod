package eatyourbeets.interfaces.subscribers;

import com.megacrit.cardcrawl.core.AbstractCreature;

public interface OnHealthBarUpdatedSubscriber
{
    void OnHealthBarUpdated(AbstractCreature creature);
}
