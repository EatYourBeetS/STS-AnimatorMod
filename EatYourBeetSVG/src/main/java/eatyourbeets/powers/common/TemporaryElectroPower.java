package eatyourbeets.powers.common;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.ElectroPower;
import eatyourbeets.utilities.GameActions;

public class TemporaryElectroPower extends AbstractTemporaryPower
{
    public static final String POWER_ID = CreateFullID(TemporaryElectroPower.class);

    public TemporaryElectroPower(AbstractCreature owner)
    {
        super(owner, 1, POWER_ID, ElectroPower::new);
    }

    @Override
    public void onRemove()
    {
        super.onRemove();

        GameActions.Bottom.RemovePower(owner, owner, ElectroPower.POWER_ID);
    }
}
