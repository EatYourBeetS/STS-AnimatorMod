package eatyourbeets.interfaces.subscribers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;

public interface OnApplyPowerSubscriber
{
    void OnApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source);
}
