package pinacolada.powers.affinity;

import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.utilities.PCLActions;

public class VelocityPower extends AbstractPCLAffinityPower
{
    public static final String POWER_ID = CreateFullID(VelocityPower.class);
    public static final PCLAffinity AFFINITY_TYPE = PCLAffinity.Green;

    public VelocityPower()
    {
        super(AFFINITY_TYPE, POWER_ID);
    }

    @Override
    public void OnUse(AbstractMonster m, int cost)
    {
        PCLActions.Bottom.Draw((int) GetEffectiveIncrease());
        flash();
    }

    @Override
    protected int GetMultiplierForDescription() {
        return (int) GetEffectiveIncrease();
    }

    @Override
    protected float GetEffectiveIncrease() {
        return super.GetEffectiveIncrease() * 2;
    }
}