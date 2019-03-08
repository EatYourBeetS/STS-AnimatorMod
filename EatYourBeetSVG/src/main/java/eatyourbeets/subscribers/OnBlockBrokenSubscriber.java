package eatyourbeets.subscribers;

import com.megacrit.cardcrawl.core.AbstractCreature;

public interface OnBlockBrokenSubscriber
{
    void OnBlockBroken(AbstractCreature creature);
}
