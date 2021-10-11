package eatyourbeets.powers.animator;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.orbs.Lightning;
import eatyourbeets.cards.base.Affinity;

public class Amplification_LightningPower extends Amplification_AbstractPower
{
    public static final Affinity AFFINITY = Affinity.Water;
    public static final String POWER_ID = CreateFullID(Amplification_LightningPower.class);
    public static final String ORB_ID = Lightning.ORB_ID;

    public Amplification_LightningPower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID, ORB_ID, AFFINITY, amount);
    }
}