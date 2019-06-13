package eatyourbeets.interfaces;

import com.megacrit.cardcrawl.core.AbstractCreature;

public interface OnBeforeLoseBlockSubscriber
{
    void OnBeforeLoseBlock(AbstractCreature creature, int amount, boolean noAnimation);
}
