package eatyourbeets.powers.unnamed;

import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.InvisiblePower;
import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.powers.UnnamedPower;

public class CleavingAttacksPower extends UnnamedPower implements InvisiblePower
{
    public static final String POWER_ID = CreateFullID(CleavingAttacksPower.class);

    public CleavingAttacksPower(AbstractCreature owner)
    {
        super(owner, POWER_ID);
    }

    @Override
    public void atStartOfTurn()
    {
        super.atStartOfTurn();

        RemovePower();
    }
}
