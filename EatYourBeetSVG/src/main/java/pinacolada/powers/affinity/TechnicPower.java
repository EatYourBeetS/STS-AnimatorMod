package pinacolada.powers.affinity;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.powers.common.GenesisPower;
import pinacolada.utilities.PCLActions;

public class TechnicPower extends AbstractPCLAffinityPower
{
    public static final String POWER_ID = CreateFullID(TechnicPower.class);
    public static final PCLAffinity AFFINITY_TYPE = PCLAffinity.Silver;

    public TechnicPower()
    {
        super(AFFINITY_TYPE, POWER_ID);
    }

    public void Initialize(AbstractCreature owner)
    {
        super.Initialize(owner);
        SetThreshold(10);
    }

    @Override
    public void OnUse(AbstractMonster m, int cost)
    {
        PCLActions.Bottom.StackPower(new GenesisPower(player, (int) GetEffectiveIncrease()));
        flash();
    }

    @Override
    protected int GetMultiplierForDescription() {
        return (int) GetEffectiveIncrease();
    }
}