package eatyourbeets.interfaces.subscribers;

import com.megacrit.cardcrawl.core.AbstractCreature;

public interface OnOrbApplyLockOnSubscriber
{
    int OnOrbApplyLockOn(int retVal, AbstractCreature target, int dmg);
}
