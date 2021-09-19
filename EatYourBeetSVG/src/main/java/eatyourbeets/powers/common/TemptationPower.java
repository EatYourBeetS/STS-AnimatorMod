package eatyourbeets.powers.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import eatyourbeets.actions.powers.ApplyPower;
import eatyourbeets.powers.CommonPower;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

public class TemptationPower extends CommonPower {
    public static final String POWER_ID = CreateFullID(TemptationPower.class);
    public static final float MULTIPLIER = 10f;
    private float percentage;

    public TemptationPower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);

        Initialize(amount);
    }

    @Override
    public void updateDescription()
    {
        this.description = FormatDescription(0, amount);
    }

    @Override
    public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source)
    {
        super.onApplyPower(power, target, source);

        for (PowerHelper commonDebuffHelper : GameUtilities.GetCommonDebuffs()) {
            if (power.ID.equals(commonDebuffHelper.ID))
            {
                power.amount += this.amount;

                final AbstractGameAction action = AbstractDungeon.actionManager.currentAction;
                if (action instanceof ApplyPower || action instanceof ApplyPowerAction)
                {
                    action.amount += this.amount;
                }
                else
                {
                    JUtils.LogWarning(this, "Unknown action type: " + action.getClass().getName());
                }
                return;
            }
        }
    }


}
