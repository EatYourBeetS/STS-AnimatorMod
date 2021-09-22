package eatyourbeets.powers.common;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.interfaces.subscribers.OnSpendEnergySubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.CommonPower;
import eatyourbeets.utilities.ColoredString;

public class DevotionPower extends CommonPower implements OnSpendEnergySubscriber
{
    public static final String POWER_ID = CreateFullID(DevotionPower.class);
    public static final int CHARGE_THRESHOLD = 8;
    protected int charge;

    public DevotionPower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);

        this.charge = 0;

        Initialize(amount);
    }

    @Override
    public void updateDescription()
    {
        this.description = FormatDescription(0, CHARGE_THRESHOLD, GetCurrentChargeCost(), GetChargeIncrease(Math.max(charge,CHARGE_THRESHOLD)) * 100f);
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

        CombatStats.onSpendEnergy.Subscribe(this);
    }

    @Override
    public void onRemove()
    {
        super.onRemove();

        CombatStats.onSpendEnergy.Unsubscribe(this);
    }

    @Override
    public float modifyBlock(float blockAmount) {
        return blockAmount * GetChargeMultiplier(this.charge);
    }

    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type) {
        return type == DamageInfo.DamageType.NORMAL ? damage * GetChargeMultiplier(this.charge) : damage;
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action)
    {
        super.onUseCard(card, action);

        if ((card.baseBlock > 0 || card.baseDamage > 0) && charge >= CHARGE_THRESHOLD)
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
        return 0.25f * this.amount * Math.floorDiv(charge, CHARGE_THRESHOLD);
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
