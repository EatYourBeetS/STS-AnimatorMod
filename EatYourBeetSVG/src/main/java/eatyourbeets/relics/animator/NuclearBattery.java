package eatyourbeets.relics.animator;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Plasma;
import eatyourbeets.interfaces.subscribers.OnEnergyRechargeSubscriber;
import eatyourbeets.powers.AnimatorClickablePower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.PowerTriggerConditionType;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.relics.EYBRelic;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import org.apache.commons.lang3.mutable.MutableInt;

public class NuclearBattery extends AnimatorRelic implements OnEnergyRechargeSubscriber
{
    public static final String ID = CreateFullID(NuclearBattery.class);
    public static int MAX_USES = 2;
    public static int ENERGY_COST = 1;

    public NuclearBattery()
    {
        super(ID, RelicTier.BOSS, LandingSound.HEAVY);
    }

    @Override
    protected void ActivateBattleEffect()
    {
        super.ActivateBattleEffect();

        CombatStats.onEnergyRecharge.Subscribe(this);
        GameActions.Bottom.ApplyPower(new NuclearBatteryPower(player, this));
        flash();
    }

    @Override
    protected void DeactivateBattleEffect()
    {
        super.DeactivateBattleEffect();

        CombatStats.onEnergyRecharge.Unsubscribe(this);
    }

    @Override
    public void OnEnergyRecharge(MutableInt previousEnergy, MutableInt currentEnergy)
    {
        if (previousEnergy.getValue() > 0)
        {
            previousEnergy.decrement();
            currentEnergy.increment();
        }
    }

    public static class NuclearBatteryPower extends AnimatorClickablePower
    {
        public NuclearBatteryPower(AbstractCreature owner, EYBRelic relic)
        {
            super(owner, relic, PowerTriggerConditionType.Energy, ENERGY_COST);

            this.amount = MAX_USES;
            this.triggerCondition.SetUses(amount, false, true)
            .SetCondition(__ -> !GameUtilities.HasOrb(Plasma.ORB_ID));
        }

        @Override
        public String GetUpdatedDescription()
        {
            return FormatDescription(1);
        }

        @Override
        public void OnUse(AbstractMonster m)
        {
            super.OnUse(m);

            GameActions.Bottom.ChannelOrb(new Plasma());
            if ((amount = triggerCondition.uses) <= 0)
            {
                RemovePower(GameActions.Last);
            }
        }
    }
}