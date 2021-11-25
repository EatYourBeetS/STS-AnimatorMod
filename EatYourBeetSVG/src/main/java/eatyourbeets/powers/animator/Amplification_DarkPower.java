package eatyourbeets.powers.animator;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.orbs.Dark;
import eatyourbeets.cards.base.Affinity;

public class Amplification_DarkPower extends Amplification_AbstractPower
{
    public static final Affinity AFFINITY = Affinity.Dark;
    public static final String POWER_ID = CreateFullID(Amplification_DarkPower.class);
    public static final String ORB_ID = Dark.ORB_ID;

    public Amplification_DarkPower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID, ORB_ID, amount, 1, AFFINITY);
    }

    public Amplification_DarkPower(AbstractCreature owner, int amount, Affinity... affinities)
    {
        super(owner, POWER_ID, ORB_ID, amount, 1, affinities);
    }
}