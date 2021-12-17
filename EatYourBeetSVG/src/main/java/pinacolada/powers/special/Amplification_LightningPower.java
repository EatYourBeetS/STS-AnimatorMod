package pinacolada.powers.special;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.orbs.Lightning;
import pinacolada.cards.base.PCLAffinity;

public class Amplification_LightningPower extends Amplification_AbstractPower
{
    public static final PCLAffinity AFFINITY = PCLAffinity.Blue;
    public static final String POWER_ID = CreateFullID(Amplification_LightningPower.class);
    public static final String ORB_ID = Lightning.ORB_ID;

    public Amplification_LightningPower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID, ORB_ID, amount, 1, AFFINITY);
    }

    public Amplification_LightningPower(AbstractCreature owner, int amount, PCLAffinity... affinities)
    {
        super(owner, POWER_ID, ORB_ID, amount, 1, affinities);
    }
}