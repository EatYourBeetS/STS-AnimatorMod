package eatyourbeets.powers.animator;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.HealthBarRenderPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import eatyourbeets.actions.common.WaitRealtimeAction;
import eatyourbeets.cards.animator.Demiurge;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActionsHelper;

public class DemiurgePower extends AnimatorPower implements HealthBarRenderPower
{
    public static final String POWER_ID = CreateFullID(DemiurgePower.class.getSimpleName());

    private static final Demiurge demiurge = new Demiurge();

    public DemiurgePower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);

        this.amount = amount;
        this.type = PowerType.DEBUFF;

        updateDescription();
    }

    @Override
    public void atEndOfTurn(boolean isPlayer)
    {
        AbstractDungeon.effectsQueue.add(new ShowCardBrieflyEffect(demiurge.makeStatEquivalentCopy()));
        GameActionsHelper.AddToBottom(new RemoveSpecificPowerAction(owner, owner, this));
        GameActionsHelper.AddToBottom(new WaitRealtimeAction(0.5f));
        GameActionsHelper.DamageTarget(owner, owner, amount, DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.SLASH_VERTICAL);

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