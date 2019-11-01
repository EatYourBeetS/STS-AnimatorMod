package eatyourbeets.powers.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.PoisonPower;
import eatyourbeets.powers.AnimatorPower;

public class MarkOfPoisonPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(MarkOfPoisonPower.class.getSimpleName());

    private final AbstractCreature source;

    public MarkOfPoisonPower(AbstractCreature owner, AbstractCreature source, int stacks)
    {
        super(owner, POWER_ID);
        this.source = source;
        this.amount = stacks;
        this.type = PowerType.DEBUFF;
        updateDescription();
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount)
    {
        if (info.type != DamageInfo.DamageType.THORNS && damageAmount > 0)
        {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(owner, source, new PoisonPower(owner, source, this.amount), this.amount, AbstractGameAction.AttackEffect.POISON));
        }

        return super.onAttacked(info, damageAmount);
    }

    @Override
    public void atEndOfTurn(boolean isPlayer)
    {
        AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(owner, source, this));

        super.atEndOfTurn(isPlayer);
    }
}
