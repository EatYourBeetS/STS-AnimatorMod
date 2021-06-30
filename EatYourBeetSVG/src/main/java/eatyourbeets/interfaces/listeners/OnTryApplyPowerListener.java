package eatyourbeets.interfaces.listeners;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;

public interface OnTryApplyPowerListener
{
    boolean TryApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source);
}
