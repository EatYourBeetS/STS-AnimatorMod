package eatyourbeets.powers.affinity;

import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.utilities.GameUtilities;

public class WisdomPower extends AbstractAffinityPower
{
    public static final String POWER_ID = CreateFullID(WisdomPower.class);
    public static final Affinity AFFINITY_TYPE = Affinity.Blue;

    public WisdomPower()
    {
        super(AFFINITY_TYPE, POWER_ID);
    }

    @Override
    public void OnUse(AbstractMonster m, int cost)
    {
        this.SetMaxAmount(maxAmount + 1);
        AbstractOrb orb = GameUtilities.GetFirstOrb(null);
        if (GameUtilities.IsValidOrb(orb)) {
            int increase = (int) GetEffectiveIncrease();
            GameUtilities.ModifyOrbBaseEvokeAmount(orb, increase, true, false);
            GameUtilities.ModifyOrbBasePassiveAmount(orb, increase, true, false);
            flash();
        }
    }

    @Override
    protected int GetMultiplierForDescription() {
        return (int) GetEffectiveIncrease();
    }

    @Override
    protected float GetEffectiveIncrease() {
        return super.GetEffectiveIncrease() * 7;
    }
}