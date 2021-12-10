package eatyourbeets.powers.affinity;

import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.powers.common.DesecratedPower;
import eatyourbeets.ui.animator.combat.EYBCardAffinityRow;
import eatyourbeets.utilities.GameActions;

import static eatyourbeets.powers.common.DesecratedPower.GetDamageDealtDecrease;
import static eatyourbeets.powers.common.DesecratedPower.GetDamageReceivedIncrease;

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
        if (m != null)
        {
            GameActions.Bottom.StackPower(new DesecratedPower(m, (int) GetEffectiveIncrease())).ShowEffect(true, true).IgnoreArtifact(true);
            flash();
        }
    }

    @Override
    public String GetUpdatedDescription()
    {
        String newDesc = FormatDescription(0, EYBCardAffinityRow.SYNERGY_MULTIPLIER, GetEffectiveThreshold(), GetDamageReceivedIncrease(GetMultiplierForDescription()) * 100, GetDamageDealtDecrease(GetMultiplierForDescription()) * 100,  !IsEnabled() ? powerStrings.DESCRIPTIONS[1] : "");
        if (this.tooltips.size() > 0) {
            this.tooltips.get(0).description = newDesc;
        }
        return newDesc;
    }

    @Override
    protected int GetMultiplierForDescription() {
        return (int) GetEffectiveIncrease();
    }

    @Override
    protected float GetEffectiveIncrease() {
        return super.GetEffectiveIncrease() * 2;
    }
}