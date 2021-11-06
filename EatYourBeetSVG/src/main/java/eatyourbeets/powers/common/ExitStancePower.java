package eatyourbeets.powers.common;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.stances.NeutralStance;
import eatyourbeets.powers.EYBClickablePower;
import eatyourbeets.powers.PowerTriggerConditionType;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class ExitStancePower extends EYBClickablePower
{
    public static final String POWER_ID = GR.Common.CreateID(ExitStancePower.class.getSimpleName());

    public ExitStancePower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID, PowerTriggerConditionType.None, 0, (__) -> !GameUtilities.InStance(NeutralStance.STANCE_ID), null);

        this.triggerCondition.SetOneUsePerPower(true);
        this.hideAmount = true;

        Initialize(amount);
    }

    @Override
    public void OnUse(AbstractMonster m, int cost)
    {
        super.OnUse(m, cost);

        GameActions.Bottom.ChangeStance(NeutralStance.STANCE_ID);
    }
}