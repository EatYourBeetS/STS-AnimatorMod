package eatyourbeets.powers.affinity;

import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.powers.common.TaintedPower;
import eatyourbeets.utilities.GameActions;

public class DesecrationPower extends AbstractAffinityPower
{
    public static final String POWER_ID = CreateFullID(DesecrationPower.class);
    public static final Affinity AFFINITY_TYPE = Affinity.Dark;

    public DesecrationPower()
    {
        super(AFFINITY_TYPE, POWER_ID);
        this.triggerCondition.requiresTarget = true;
    }

    @Override
    public void OnUse(AbstractMonster m, int cost)
    {
        this.SetMaxAmount(maxAmount + 1);
        if (m != null)
        {
            GameActions.Bottom.StackPower(new TaintedPower(m, (int) GetEffectiveIncrease())).ShowEffect(true, true).IgnoreArtifact(true);
            flash();
        }
    }

    @Override
    protected int GetMultiplierForDescription() {
        return (int) GetEffectiveIncrease();
    }
}