package eatyourbeets.powers.common;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.HealthBarRenderPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import eatyourbeets.utilities.GameActionsHelper_Legacy;

public class PoisonPlayerPower extends AbstractPower implements HealthBarRenderPower
{
    private static final Color healthBarColor = Color.valueOf("78c13c");
    public static final String POWER_ID = "PoisonPlayer";
    private static final PowerStrings powerStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;

    private AbstractCreature source;

    public PoisonPlayerPower(AbstractCreature owner, AbstractCreature source, int poisonAmt)
    {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.source = source;
        this.amount = poisonAmt;
        if (this.amount >= 9999)
        {
            this.amount = 9999;
        }

        this.updateDescription();
        this.loadRegion("poison");
        this.type = PowerType.DEBUFF;
        this.isTurnBased = true;
    }

    public void playApplyPowerSfx()
    {
        CardCrawlGame.sound.play("POWER_POISON", 0.05F);
    }

    public void updateDescription()
    {
        if (this.owner != null && !this.owner.isPlayer)
        {
            this.description = DESCRIPTIONS[2] + this.amount + DESCRIPTIONS[1];
        }
        else
        {
            this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
        }

    }

    public void atStartOfTurn()
    {
        this.flashWithoutSound();
        GameActionsHelper_Legacy.DamageTarget(source, owner, this.amount, DamageInfo.DamageType.HP_LOSS, AttackEffect.POISON);
        GameActionsHelper_Legacy.AddToBottom(new ReducePowerAction(owner, owner, this, 1));
    }

    @Override
    public int getHealthBarAmount()
    {
        return amount;
    }

    @Override
    public Color getColor()
    {
        return healthBarColor;
    }

    static
    {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings("Poison");
        NAME = powerStrings.NAME;
        DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    }
}
