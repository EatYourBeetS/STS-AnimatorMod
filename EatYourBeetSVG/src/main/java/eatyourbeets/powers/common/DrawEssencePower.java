package eatyourbeets.powers.common;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.powers.CommonClickablePower;
import eatyourbeets.powers.PowerTriggerConditionType;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.InputManager;

public class DrawEssencePower extends CommonClickablePower
{
    public static final String POWER_ID = CreateFullID(DrawEssencePower.class);
    public static final int REQUIRED_AMOUNT = 3;

    public DrawEssencePower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID, PowerTriggerConditionType.Special, REQUIRED_AMOUNT);

        this.canBeZero = true;
        this.triggerCondition.SetCondition(v -> this.amount >= v);
        this.triggerCondition.SetPayCost(this::ReducePower);
        this.triggerCondition.SetUses(-1, false, false);

        Initialize(amount);
    }

    @Override
    public String GetUpdatedDescription()
    {
        return FormatDescription(0, triggerCondition.requiredAmount);
    }

    @Override
    public void update(int slot)
    {
        super.update(slot);

        if (GameUtilities.CanAcceptInput(false) && InputManager.Control.IsJustPressed())
        {
            TryClick();
        }
    }

    @Override
    public void OnUse(AbstractMonster m)
    {
        super.OnUse(m);

        GameActions.Bottom.Draw(1);
        flashWithoutSound();
    }
}
