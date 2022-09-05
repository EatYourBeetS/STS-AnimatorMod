package eatyourbeets.powers.animatorClassic;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.PoisonPower;
import eatyourbeets.actions.powers.ApplyPower;
import eatyourbeets.powers.AnimatorClassicPower;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

public class PoisonAffinityPower extends AnimatorClassicPower
{
    public static final String POWER_ID = CreateFullID(PoisonAffinityPower.class);

    public PoisonAffinityPower(AbstractCreature owner, int stacks)
    {
        super(owner, POWER_ID);

        this.amount = stacks;
        this.type = PowerType.BUFF;
        updateDescription();
    }

    @Override
    public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source)
    {
        super.onApplyPower(power, target, source);

        if (GameUtilities.IsPlayer(source) && power.ID.equals(PoisonPower.POWER_ID))
        {
            power.amount += this.amount;

            AbstractGameAction action = AbstractDungeon.actionManager.currentAction;
            if (action instanceof ApplyPower || action instanceof ApplyPowerAction)
            {
                action.amount += this.amount;
            }
            else
            {
                JUtils.LogWarning(this, "Unknown action type: " + action.getClass().getName());
            }
        }
    }
}
