package eatyourbeets.interfaces.subscribers;

import com.megacrit.cardcrawl.core.AbstractCreature;

public interface OnBlockGainedSubscriber
{
    void OnBlockGained(AbstractCreature creature, int block);
}
