package eatyourbeets.interfaces.subscribers;

import com.megacrit.cardcrawl.powers.AbstractPower;

public interface OnModifyDebuffSubscriber
{
    void OnModifyDebuff(AbstractPower power, int initialAmount, int newAmount);
} 