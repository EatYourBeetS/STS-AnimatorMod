package eatyourbeets.interfaces.subscribers;

import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.powers.EYBClickablePower;

public interface OnClickablePowerUsedSubscriber
{
    void OnClickablePowerUsed(EYBClickablePower power, AbstractMonster target);
}