package pinacolada.powers.affinity;

import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.powers.common.DesecratedPower;
import pinacolada.ui.combat.PCLAffinityRow;
import pinacolada.utilities.PCLActions;

import static pinacolada.powers.common.DesecratedPower.GetDamageDealtDecrease;
import static pinacolada.powers.common.DesecratedPower.GetDamageReceivedIncrease;

public class DesecrationPower extends AbstractPCLAffinityPower
{
    public static final String POWER_ID = CreateFullID(DesecrationPower.class);
    public static final PCLAffinity AFFINITY_TYPE = PCLAffinity.Dark;

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
            PCLActions.Bottom.StackPower(new DesecratedPower(m, (int) GetEffectiveIncrease())).ShowEffect(true, true).IgnoreArtifact(true);
            flash();
        }
    }

    @Override
    public String GetUpdatedDescription()
    {
        String newDesc = FormatDescription(0, PCLAffinityRow.SYNERGY_MULTIPLIER, GetEffectiveThreshold(), GetNextGrantingLevel(), GetDamageReceivedIncrease(GetMultiplierForDescription()) * 100, GetDamageDealtDecrease(GetMultiplierForDescription()) * 100,  !IsEnabled() ? powerStrings.DESCRIPTIONS[1] : "");
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