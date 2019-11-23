package eatyourbeets.powers.common;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.HealthBarRenderPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import eatyourbeets.powers.CommonPower;
import eatyourbeets.utilities.GameActionsHelper;

public class SelfDamagePower extends CommonPower implements HealthBarRenderPower
{
    public static final String POWER_ID = CreateFullID(SelfDamagePower.class.getSimpleName());

    public SelfDamagePower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);

        this.amount = amount;
        this.type = PowerType.DEBUFF;
        this.priority = 105;

        updateDescription();
    }

    public void playApplyPowerSfx()
    {
        CardCrawlGame.sound.play("BLOOD_SPLAT", 0.05F);
    }

    @Override
    public void atEndOfTurn(boolean isPlayer)
    {
        GameActionsHelper.DamageTarget(null, owner, amount, DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.SLASH_VERTICAL);
        GameActionsHelper.AddToBottom(new RemoveSpecificPowerAction(owner, owner, this));

        playApplyPowerSfx();
        flashWithoutSound();

        super.atEndOfTurn(isPlayer);
    }

    @Override
    public int getHealthBarAmount()
    {
        return Math.max(0, amount - owner.currentBlock);
    }

    @Override
    public Color getColor()
    {
        return Color.PURPLE;
    }
}