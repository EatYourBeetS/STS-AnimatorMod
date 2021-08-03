package eatyourbeets.powers.animator;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.HealthBarRenderPower;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;

import java.text.DecimalFormat;

public class ChilledPower extends AnimatorPower implements HealthBarRenderPower
{
    private static final Color healthBarColor = Color.SKY.cpy();
    public static final String POWER_ID = CreateFullID(ChilledPower.class);
    public static final int MAX_REDUCTION_STACKS = 20;
    public static final float RATE = 0.5f;

    private float percentage;
    private final AbstractCreature source;

    public static float CalculatePercentage(int amount)
    {
        return 90f - (0.f * Math.min(MAX_REDUCTION_STACKS,amount));
    }

    public ChilledPower(AbstractCreature owner, AbstractCreature source, int amount)
    {
        super(owner, POWER_ID);

        this.source = source == null ? owner : source;
        this.amount = amount;
        if (this.amount >= 9999)
        {
            this.amount = 9999;
        }
        this.type = PowerType.DEBUFF;
        this.isTurnBased = true;

        updatePercentage();
    }

    @Override
    public void updateDescription()
    {
        if (amount > 0)
        {
            DecimalFormat df = new DecimalFormat("#.0");
            String value = df.format(((1 - this.percentage) * 100));

            this.description = powerStrings.DESCRIPTIONS[0] + value + powerStrings.DESCRIPTIONS[1];
        }
    }

    @Override
    public void playApplyPowerSfx()
    {
        CardCrawlGame.sound.playA("ORB_FROST_CHANNEL", -0.25f);
    }

    @Override
    public void atStartOfTurn()
    {
        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && !AbstractDungeon.getMonsters().areMonstersBasicallyDead())
        {
            this.flashWithoutSound();

            GameActions.Bottom.DealDamage(source, owner, getHealthBarAmount(), DamageInfo.DamageType.HP_LOSS, AttackEffects.SHIELD_FROST);
            GameActions.Bottom.ReducePower(this, 1);
        }
    }

    @Override
    public void stackPower(int stackAmount)
    {
        super.stackPower(stackAmount);
        updatePercentage();
    }

    @Override
    public void reducePower(int reduceAmount)
    {
        super.reducePower(reduceAmount);
        updatePercentage();
    }

    @Override
    public void atEndOfRound()
    {
        super.atEndOfRound();
        GameActions.Bottom.ReducePower(this, Math.max(MathUtils.ceil(this.amount * 0.75f),1));
    }

    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type)
    {
        float newDamage = calculateDamageGiven(damage, type);
        return super.atDamageGive(newDamage, type);
    }

    @Override
    public int getHealthBarAmount()
    {
        if (amount == 1)
        {
            return 1;
        }
        else if (amount > 1)
        {
            return (amount / 2) + amount % 2;
        }
        else
        {
            return 0;
        }
    }

    @Override
    public Color getColor()
    {
        return healthBarColor;
    }

    public int calculateDamageGiven(float damage, DamageInfo.DamageType type)
    {
        if (type == DamageInfo.DamageType.NORMAL)
        {
            float multiplier = (owner.hasPower(WeakPower.POWER_ID)) ? CalculatePercentage(this.amount / 2) : percentage;
            return MathUtils.ceil(multiplier * damage);
        }
        return (int) damage;
    }

    private void updatePercentage()
    {
        percentage = CalculatePercentage(this.amount);
        updateDescription();
    }
}
