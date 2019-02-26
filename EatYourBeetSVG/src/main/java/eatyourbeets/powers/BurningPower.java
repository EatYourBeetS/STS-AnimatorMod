package eatyourbeets.powers;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.HealthBarRenderPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.unique.PoisonLoseHpAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;

public class BurningPower extends AnimatorPower implements HealthBarRenderPower
{
    public static final String POWER_ID = CreateFullID(BurningPower.class.getSimpleName());
    private AbstractCreature source;

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
        this.description = powerStrings.DESCRIPTIONS[0] + this.amount + powerStrings.DESCRIPTIONS[1] + (this.amount * 5) + powerStrings.DESCRIPTIONS[2];
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
            GameActionsHelper.DamageTarget(this.source, this.owner, this.amount, DamageInfo.DamageType.HP_LOSS, AbstractGameAction.AttackEffect.FIRE);
        }
    }

    @Override
    public float atDamageReceive(float damage, DamageInfo.DamageType type)
    {
        if (type == DamageInfo.DamageType.NORMAL)
        {
            return Math.round(damage * ((100 + this.amount * 5) / 100f));
        }
        else
        {
            return damage;
        }
    }

    @Override
    public int getHealthBarAmount()
    {
        return this.amount;
    }

    @Override
    public Color getColor()
    {
        return Color.ORANGE;
    }
}
