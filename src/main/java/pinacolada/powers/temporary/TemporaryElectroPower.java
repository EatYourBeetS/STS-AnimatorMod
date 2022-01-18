package pinacolada.powers.temporary;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.ElectroPower;
import pinacolada.powers.common.AbstractTemporaryPower;
import pinacolada.utilities.PCLActions;

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

        PCLActions.Bottom.RemovePower(owner, owner, ElectroPower.POWER_ID);
    }
}
