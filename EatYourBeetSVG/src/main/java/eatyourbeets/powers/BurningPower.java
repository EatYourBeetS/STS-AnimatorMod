package eatyourbeets.powers;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.HealthBarRenderPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import eatyourbeets.GameActionsHelper;

public class BurningPower extends AnimatorPower implements HealthBarRenderPower
{
    public static final String POWER_ID = CreateFullID(BurningPower.class.getSimpleName());
    private final AbstractCreature source;

    private static final float ATTACK_MULTIPLIER = 4;

    public BurningPower(AbstractCreature owner, AbstractCreature source, int amount)
    {
        super(owner, POWER_ID);

        this.source = source;
        this.amount = amount;

        if (this.amount >= 9999)
        {
            this.amount = 9999;
        }

        this.type = PowerType.DEBUFF;

        updateDescription();
    }

    @Override
    public void updateDescription()
    {
        this.description = powerStrings.DESCRIPTIONS[0] + getHealthBarAmount() + powerStrings.DESCRIPTIONS[1] + (int)(this.amount * ATTACK_MULTIPLIER) + powerStrings.DESCRIPTIONS[2];
    }

    @Override
    public void playApplyPowerSfx()
    {
        CardCrawlGame.sound.play("ATTACK_FIRE", 0.05F);
    }

    @Override
    public void atStartOfTurn()
    {
        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && !AbstractDungeon.getMonsters().areMonstersBasicallyDead())
        {
            this.flashWithoutSound();
            GameActionsHelper.DamageTarget(this.source, this.owner, getHealthBarAmount(), DamageInfo.DamageType.HP_LOSS, AbstractGameAction.AttackEffect.FIRE);
            GameActionsHelper.AddToBottom(new ReducePowerAction(owner, owner, this, 1));
        }
    }

    @Override
    public float atDamageReceive(float damage, DamageInfo.DamageType type)
    {
        if (type == DamageInfo.DamageType.NORMAL)
        {
            float multiplier = ATTACK_MULTIPLIER;
            if (owner.hasPower(VulnerablePower.POWER_ID))
            {
                multiplier *= 0.5f;
            }

            return Math.round(damage * ((100 + this.amount * multiplier) / 100f));
        }
        else
        {
            return damage;
        }
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
            return amount / 2;
        }
        else
        {
            return 0;
        }
    }

    @Override
    public Color getColor()
    {
        return Color.ORANGE;
    }
}
