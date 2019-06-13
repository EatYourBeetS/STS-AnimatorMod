package eatyourbeets.powers.animator;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class RoryMercuryPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(RoryMercuryPower.class.getSimpleName());

    public RoryMercuryPower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);

        this.amount = amount;
        updateDescription();
    }

    @Override
    public void atEndOfTurn(boolean isPlayer)
    {
        AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, this));

        super.atEndOfTurn(isPlayer);
    }

//    @Override
//    public void onAfterCardPlayed(AbstractCard usedCard)
//    {
//        super.onAfterCardPlayed(usedCard);
//
//        if (usedCard.type == AbstractCard.CardType.ATTACK)
//        {
//            AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(this.owner, this.owner, this, 1));
//        }
//    }

    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type)
    {
        if (type == DamageInfo.DamageType.NORMAL)
        {
            return Math.round(damage * (1 + amount / 100f));
        }
        else
        {
            return damage;
        }
    }
}
