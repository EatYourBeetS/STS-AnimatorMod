package pinacolada.powers.special;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Dark;
import pinacolada.cards.base.PCLAffinity;

public class Amplification_DarkPower extends Amplification_AbstractPower
{
    public static final PCLAffinity AFFINITY = PCLAffinity.Dark;
    public static final String POWER_ID = CreateFullID(Amplification_DarkPower.class);
    public static final String ORB_ID = Dark.ORB_ID;

    public Amplification_DarkPower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID, ORB_ID, amount, 1, AFFINITY);
    }

    public Amplification_DarkPower(AbstractCreature owner, int amount, PCLAffinity... affinities)
    {
        super(owner, POWER_ID, ORB_ID, amount, 1, affinities);
    }

    @Override
    public void OnApplyFocus(AbstractOrb orb)
    {
        if (orb != null && orb.ID.equals(orbID)) {
            orb.passiveAmount += GetScaledIncrease();
        }
    }
}