package pinacolada.powers.affinity;

import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.utilities.PCLGameUtilities;

public class WisdomPower extends AbstractPCLAffinityPower
{
    public static final String POWER_ID = CreateFullID(WisdomPower.class);
    public static final PCLAffinity AFFINITY_TYPE = PCLAffinity.Blue;

    public WisdomPower()
    {
        super(AFFINITY_TYPE, POWER_ID);
    }

    @Override
    public void OnUse(AbstractMonster m, int cost)
    {
        AbstractOrb orb = PCLGameUtilities.GetFirstOrb(null);
        if (PCLGameUtilities.IsValidOrb(orb)) {
            int increase = (int) GetEffectiveIncrease();
            PCLGameUtilities.ModifyOrbFocus(orb, increase, true, false);
            flash();
        }
    }

    @Override
    protected int GetMultiplierForDescription() {
        return (int) GetEffectiveIncrease();
    }

    @Override
    protected float GetEffectiveIncrease() {
        return super.GetEffectiveIncrease() * 6;
    }
}