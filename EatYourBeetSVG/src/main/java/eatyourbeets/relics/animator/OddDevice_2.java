package eatyourbeets.relics.animator;

import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.InvisiblePower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.PoisonPower;
import eatyourbeets.actions.powers.ApplyPower;
import eatyourbeets.interfaces.listeners.OnTryApplyPowerListener;
import eatyourbeets.interfaces.markers.Hidden;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.common.BurningPower;
import eatyourbeets.powers.common.DelayedDamagePower;
import eatyourbeets.utilities.JUtils;

public class OddDevice_2 extends OddDevice
{
    public static final String ID = CreateFullID(OddDevice_2.class);
    public static final int DD_SELF = 1;
    public static final int DD_ENEMY = 2;
    public static final int BURNING = 1;
    public static final int POISON = 1;

    public OddDevice_2()
    {
        super(ID, RelicTier.UNCOMMON, LandingSound.CLINK);
    }

    @Override
    protected String GetMainDescription(int effectIndex)
    {
        if (effectIndex == 1)
        {
            return FormatDescription(effectIndex, DD_SELF, DD_ENEMY);
        }
        if (effectIndex == 2)
        {
            return FormatDescription(effectIndex, BURNING);
        }
        if (effectIndex == 3)
        {
            return FormatDescription(effectIndex, POISON);
        }

        throw new RuntimeException("Invalid index: " + effectIndex);
    }

    public static class OddDevice_1Power extends AnimatorPower implements Hidden, InvisiblePower, OnTryApplyPowerListener
    {
        private int index;

        public OddDevice_1Power(AbstractCreature owner, OddDevice_1 relic)
        {
            super(owner, relic);

            this.index = relic.GetEffectIndex();
            this.canBeZero = true;
        }

        @Override
        public void updateDescription()
        {
            this.description = "";
        }

        @Override
        public boolean TryApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source, AbstractGameAction action)
        {
            if (index == 1)
            {
                if (power.ID.equals(DelayedDamagePower.POWER_ID))
                {
                    ModifyPowerAmount(power, action, target.isPlayer ? -DD_SELF : +DD_ENEMY);
                }
            }
            else if (index == 2)
            {
                if (power.ID.equals(BurningPower.POWER_ID))
                {
                    ModifyPowerAmount(power, action, BURNING);
                }
            }
            else if (index == 3)
            {
                if (power.ID.equals(PoisonPower.POWER_ID))
                {
                    ModifyPowerAmount(power, action, POISON);
                }
            }

            return action.amount > 0;
        }

        private void ModifyPowerAmount(AbstractPower power, AbstractGameAction action, int amount)
        {
            power.amount += amount;

            if (action instanceof ApplyPower || action instanceof ApplyPowerAction)
            {
                action.amount += amount;
            }
            else
            {
                JUtils.LogWarning(this, "Unknown action type: " + action.getClass().getName());
            }
        }
    }
}