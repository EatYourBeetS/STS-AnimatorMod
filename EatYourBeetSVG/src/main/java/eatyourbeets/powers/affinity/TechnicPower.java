package eatyourbeets.powers.affinity;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.powers.common.GenesisPower;
import eatyourbeets.utilities.GameActions;

public class TechnicPower extends AbstractAffinityPower
{
    public static final String POWER_ID = CreateFullID(TechnicPower.class);
    public static final Affinity AFFINITY_TYPE = Affinity.Silver;

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
        GameActions.Bottom.StackPower(new GenesisPower(player, (int) GetEffectiveIncrease()));
        flash();
    }

    @Override
    protected int GetMultiplierForDescription() {
        return (int) GetEffectiveIncrease();
    }
}