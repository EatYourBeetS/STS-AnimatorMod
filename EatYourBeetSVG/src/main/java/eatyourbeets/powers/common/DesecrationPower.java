package eatyourbeets.powers.common;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.interfaces.subscribers.OnTrySpendAffinitySubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.CommonPower;
import eatyourbeets.utilities.ColoredString;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.TargetHelper;

import java.util.UUID;

public class DesecrationPower extends CommonPower implements OnTrySpendAffinitySubscriber
{
    public static final String POWER_ID = CreateFullID(DesecrationPower.class);
    public static final int BASE_CHARGE_THRESHOLD = 6;
    public static int CHARGE_THRESHOLD = BASE_CHARGE_THRESHOLD;
    private static UUID battleID;
    public int charge;

    public static void AddChargeThreshold(int increase)
    {
        if (CombatStats.BattleID != battleID)
        {
            battleID = CombatStats.BattleID;
            CHARGE_THRESHOLD = BASE_CHARGE_THRESHOLD;
        }

        if (increase > 0)
        {
            CHARGE_THRESHOLD += increase;

            for (DesecrationPower p : GameUtilities.<DesecrationPower>GetPowers(TargetHelper.Enemies(), POWER_ID))
            {
                p.updateDescription();
                p.flashWithoutSound();
            }
        }
    }

    public DesecrationPower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);

        Initialize(amount);
    }

    @Override
    public void onInitialApplication()
    {
        super.onInitialApplication();
        CombatStats.onTrySpendAffinity.Subscribe(this);
    }

    @Override
    public void updateDescription()
    {
        this.description = FormatDescription(0, amount, GetCurrentChargeCost(), GetDebuffCount(Math.max(charge,CHARGE_THRESHOLD)), !enabled ? powerStrings.DESCRIPTIONS[1] : "");
    }

    @Override
    protected ColoredString GetSecondaryAmount(Color c)
    {
        return new ColoredString(charge, charge >= CHARGE_THRESHOLD ? Color.YELLOW : Color.WHITE, c.a);
    }

    @Override
    public void onRemove()
    {
        super.onRemove();
        CombatStats.onTrySpendAffinity.Unsubscribe(this);
    }

    @Override
    public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source)
    {
        super.onApplyPower(power, target, source);

        if (power.type == PowerType.DEBUFF && power.owner == this.owner && !power.owner.hasPower(ArtifactPower.POWER_ID))
        {
            GameActions.Last.Callback(() -> {
                this.charge += power.amount * amount;
                this.flash();
            });
        }
    }

    @Override
    public int OnTrySpendAffinity(Affinity affinity, int amount, boolean canUseStar, boolean isActuallySpending) {
        if (isActuallySpending) {
            GameActions.Last.Callback(() -> {
                this.charge += this.amount * amount;
                this.flash();
            });
        }
        return amount;
    }

    @Override
    protected void onAmountChanged(int previousAmount, int difference)
    {
        super.onAmountChanged(previousAmount, difference);
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (enabled && charge >= CHARGE_THRESHOLD && (!(card instanceof AnimatorCard) || ((AnimatorCard) card).cardData.CanTriggerSupercharge) && action.target instanceof AbstractMonster && !GameUtilities.IsDeadOrEscaped(action.target)) {
            for (int i = 0; i < GetDebuffCount(charge); i++) {
                GameActions.Bottom.StackPower(TargetHelper.Normal(action.target), GameUtilities.GetRandomElement(GameUtilities.GetCommonDebuffs()), 1)
                        .ShowEffect(true, true);
            }
            this.charge -= GetCurrentChargeCost();
            updateDescription();
            flash();
        }
    }

    private int GetCurrentChargeCost() {return Math.max(CHARGE_THRESHOLD, Math.floorDiv(charge, CHARGE_THRESHOLD) * CHARGE_THRESHOLD);}

    private int GetDebuffCount(int charge) {
        return Math.floorDiv(charge, BASE_CHARGE_THRESHOLD);
    }
}
