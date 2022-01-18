package pinacolada.interfaces.subscribers;

import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.powers.PCLClickablePower;

public interface OnPCLClickablePowerUsed
{
    boolean OnClickablePowerUsed(PCLClickablePower power, AbstractMonster target);
}