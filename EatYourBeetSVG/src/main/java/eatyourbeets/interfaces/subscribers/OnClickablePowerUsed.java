package eatyourbeets.interfaces.subscribers;

import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.powers.EYBClickablePower;

public interface OnClickablePowerUsed
{
    boolean OnClickablePowerUsed(EYBClickablePower power, AbstractMonster target);
}