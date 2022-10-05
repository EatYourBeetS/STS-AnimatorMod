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
import eatyourbeets.utilities.GameActions;
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
    protected void ActivateBattleEffect()
    {
        super.ActivateBattleEffect();

        GameActions.Bottom.StackPower(new OddDevice_2Power(player, this, 1)).ShowEffect(false, true);
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

    public static class OddDevice_2Power extends AnimatorPower implements Hidden, InvisiblePower, OnTryApplyPowerListener
    {
        private final OddDevice_2 relic;
        private final int index;

        public OddDevice_2Power(AbstractCreature owner, OddDevice_2 relic, int amount)
        {
            super(owner, relic);

            this.relic = relic;
            this.index = relic.GetEffectIndex();
            this.ID += "_" + index;

            Initialize(amount);
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
                    ModifyPowerAmount(power, action, amount * (target.isPlayer ? -DD_SELF : +DD_ENEMY));
                }
            }
            else if (index == 2)
            {
                if (power.ID.equals(BurningPower.POWER_ID))
                {
                    ModifyPowerAmount(power, action, amount * BURNING);
                }
            }
            else if (index == 3)
            {
                if (power.ID.equals(PoisonPower.POWER_ID))
                {
                    ModifyPowerAmount(power, action, amount * POISON);
                }
            }

            return action.amount > 0;
        }

        private void ModifyPowerAmount(AbstractPower power, AbstractGameAction action, int amount)
        {
            relic.flash();
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

        @Override
        public AbstractPower makeCopy()
        {
            return new OddDevice_2Power(owner, relic, amount);
        }
    }
}