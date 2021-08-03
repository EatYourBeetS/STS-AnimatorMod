package eatyourbeets.powers.replacement;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.HealthBarRenderPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.PoisonPower;
import eatyourbeets.ui.animator.combat.CombatHelper;
import eatyourbeets.utilities.GameActions;

public class PoisonPlayerPower extends AbstractPower implements HealthBarRenderPower
{
    private static final Color healthBarColor = Color.valueOf("78c13c");
    private static final PowerStrings powerStrings;

    public static final String POWER_ID = "PoisonPlayer";
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
        CardCrawlGame.sound.play("POWER_POISON", 0.05f);
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

        GameActions.Bottom.LoseHP(source, owner, this.amount, AttackEffect.POISON).CanKill(!owner.isPlayer);
        GameActions.Bottom.ReducePower(this, 1);
    }

    @Override
    public int getHealthBarAmount()
    {
        return CombatHelper.GetHealthBarAmount(owner, amount, false, true);
    }

    @Override
    public Color getColor()
    {
        return healthBarColor;
    }

    static
    {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(PoisonPower.POWER_ID);
        NAME = powerStrings.NAME;
        DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    }
}
