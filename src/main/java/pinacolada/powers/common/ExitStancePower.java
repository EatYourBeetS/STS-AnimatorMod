package pinacolada.powers.common;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.stances.NeutralStance;
import pinacolada.powers.PCLClickablePower;
import pinacolada.powers.PowerTriggerConditionType;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class ExitStancePower extends PCLClickablePower
{
    public static final String POWER_ID = GR.PCL.CreateID(ExitStancePower.class.getSimpleName());

    public ExitStancePower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID, PowerTriggerConditionType.None, 0, (__) -> !PCLGameUtilities.InStance(NeutralStance.STANCE_ID), null);

        this.triggerCondition.SetOneUsePerPower(true);
        this.hideAmount = true;

        Initialize(amount);
    }

    @Override
    public void OnUse(AbstractMonster m, int cost)
    {
        super.OnUse(m, cost);

        PCLActions.Bottom.ChangeStance(NeutralStance.STANCE_ID);
    }
}