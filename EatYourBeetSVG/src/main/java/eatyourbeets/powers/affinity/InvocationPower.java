package eatyourbeets.powers.affinity;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import eatyourbeets.actions.powers.ApplyPower;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.interfaces.subscribers.OnApplyPowerSubscriber;
import eatyourbeets.interfaces.subscribers.OnGainTempHPSubscriber;
import eatyourbeets.interfaces.subscribers.OnGainTriggerablePowerBonusSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.CommonTriggerablePower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class InvocationPower extends AbstractAffinityPower implements OnApplyPowerSubscriber, OnGainTempHPSubscriber, OnGainTriggerablePowerBonusSubscriber
{
    public static final String POWER_ID = CreateFullID(InvocationPower.class);
    public static final Affinity AFFINITY_TYPE = Affinity.Light;

    public InvocationPower()
    {
        super(AFFINITY_TYPE, POWER_ID);
    }

    @Override
    public void OnUse(AbstractMonster m, int cost)
    {
        super.OnUse(m, cost);
        CombatStats.onApplyPower.Subscribe(this);
        CombatStats.onGainTempHP.Subscribe(this);
        CombatStats.onGainTriggerablePowerBonus.Subscribe(this);
    }

    @Override
    public void OnApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source) {
        if (!isActive) {
            CombatStats.onApplyPower.Unsubscribe(this);
            CombatStats.onGainTempHP.Unsubscribe(this);
            CombatStats.onGainTriggerablePowerBonus.Unsubscribe(this);
            return;
        }
        int applyAmount = (int) (power.amount * (GetEffectiveMultiplier() - 1));

        if (GameUtilities.IsPlayer(power.owner) && GameUtilities.IsCommonBuff(power)) {
            GameActions.Last.Callback(() -> {
                AbstractPower po = GameUtilities.GetPower(power.owner, power.ID);
                if (po != null) {
                    po.stackPower(applyAmount);
                    po.flash();
                    po.updateDescription();
                }
            });

            final AbstractGameAction action = AbstractDungeon.actionManager.currentAction;
            if (action instanceof ApplyPower || action instanceof ApplyPowerAction)
            {
                action.amount *= GetEffectiveMultiplier();
            }

            isActive = false;
            flash();
        }
    }

    @Override
    public int OnGainTempHP(int amount) {
        CombatStats.onApplyPower.Unsubscribe(this);
        CombatStats.onGainTempHP.Unsubscribe(this);
        CombatStats.onGainTriggerablePowerBonus.Unsubscribe(this);
        if (!isActive) {
            return amount;
        }
        isActive = false;
        flash();
        return amount *= GetEffectiveMultiplier();
    }

    @Override
    public int OnGainTriggerablePowerBonus(String powerID, CommonTriggerablePower.Type gainType, int amount) {
        CombatStats.onApplyPower.Unsubscribe(this);
        CombatStats.onGainTempHP.Unsubscribe(this);
        CombatStats.onGainTriggerablePowerBonus.Unsubscribe(this);
        if (!isActive) {
            return amount;
        }
        isActive = false;
        flash();
        return amount *= GetEffectiveMultiplier();
    }
}