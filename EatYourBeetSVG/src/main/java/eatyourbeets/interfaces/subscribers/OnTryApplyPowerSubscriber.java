package eatyourbeets.interfaces.subscribers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;

public interface OnTryApplyPowerSubscriber
{
    boolean TryApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source);
}
