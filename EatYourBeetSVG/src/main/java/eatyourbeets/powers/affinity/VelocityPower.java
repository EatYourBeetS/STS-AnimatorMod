package eatyourbeets.powers.affinity;

import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.utilities.GameActions;

public class VelocityPower extends AbstractAffinityPower
{
    public static final String POWER_ID = CreateFullID(VelocityPower.class);
    public static final Affinity AFFINITY_TYPE = Affinity.Green;

    public VelocityPower()
    {
        super(AFFINITY_TYPE, POWER_ID);
    }

    @Override
    public void OnUse(AbstractMonster m, int cost)
    {
        GameActions.Bottom.Draw((int) GetEffectIncrease());
        flash();
    }

    @Override
    protected int GetMultiplierForDescription() {
        return (int) GetEffectIncrease();
    }

    @Override
    protected float GetEffectIncrease() {
        return super.GetEffectIncrease() * 2;
    }
}