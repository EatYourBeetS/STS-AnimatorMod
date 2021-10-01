package eatyourbeets.powers.common;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.interfaces.subscribers.OnSpendEnergySubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.CommonPower;
import eatyourbeets.utilities.ColoredString;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.TargetHelper;

import java.util.UUID;

public class SuperchargedPower extends CommonPower implements OnSpendEnergySubscriber
{
    public static final String POWER_ID = CreateFullID(SuperchargedPower.class);
    public static final int BASE_CHARGE_THRESHOLD = 8;
    public static int CHARGE_THRESHOLD = BASE_CHARGE_THRESHOLD;
    public int charge;
    private static UUID battleID;

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

            for (SuperchargedPower p : GameUtilities.<SuperchargedPower>GetPowers(TargetHelper.Enemies(), POWER_ID))
            {
                p.updateDescription();
                p.flashWithoutSound();
            }
        }
    }

    public SuperchargedPower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);

        this.charge = 0;

        Initialize(amount);
    }

    @Override
    public void updateDescription()
    {
        this.description = FormatDescription(0, CHARGE_THRESHOLD, GetCurrentChargeCost(), GetChargeIncrease(Math.max(charge,CHARGE_THRESHOLD)) * 100f, !enabled ? powerStrings.DESCRIPTIONS[1] : null);
    }

    @Override
    protected ColoredString GetSecondaryAmount(Color c)
    {
        return new ColoredString(charge, charge >= CHARGE_THRESHOLD ? Color.YELLOW : Color.WHITE, c.a);
    }

    @Override
    public void onInitialApplication()
    {
        super.onInitialApplication();

        AddChargeThreshold(0);
        CombatStats.onSpendEnergy.Subscribe(this);
    }

    @Override
    public void onRemove()
    {
        super.onRemove();

        CombatStats.onSpendEnergy.Unsubscribe(this);
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        super.atEndOfTurn(isPlayer);

        enabled = true;
    }

    @Override
    public float modifyBlock(float blockAmount) {
        return enabled ? blockAmount * GetChargeMultiplier(this.charge) : blockAmount;
    }

    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type) {
        return enabled && type == DamageInfo.DamageType.NORMAL ? damage * GetChargeMultiplier(this.charge) : damage;
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action)
    {
        super.onUseCard(card, action);

        if (enabled && (card.baseBlock > 0 || card.baseDamage > 0) && charge >= CHARGE_THRESHOLD && (!(card instanceof AnimatorCard) || ((AnimatorCard) card).cardData.CanTriggerSupercharge))
        {
            this.charge -= GetCurrentChargeCost();
            updateDescription();
            flash();
        }
    }

    @Override
    public void onAfterUseCard(AbstractCard card, UseCardAction action) {
        this.charge += card.cost;
        this.updateDescription();
    }

    private float GetChargeIncrease(int charge) {
        return 0.25f * this.amount * Math.floorDiv(charge, BASE_CHARGE_THRESHOLD);
    }

    private float GetChargeMultiplier(int charge) {
        return 1f + GetChargeIncrease(charge);
    }

    private int GetCurrentChargeCost() {return Math.max(CHARGE_THRESHOLD, Math.floorDiv(charge, CHARGE_THRESHOLD) * CHARGE_THRESHOLD);}

    @Override
    public int OnSpendEnergy(int spendAmount) {
        this.charge += spendAmount;
        this.updateDescription();
        return spendAmount;
    }
}
