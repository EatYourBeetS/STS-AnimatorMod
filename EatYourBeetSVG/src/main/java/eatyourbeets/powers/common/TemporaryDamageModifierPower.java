package eatyourbeets.powers.common;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.powers.CommonPower;

public class TemporaryDamageModifierPower extends CommonPower
{
    public static final String POWER_ID = CreateFullID(TemporaryDamageModifierPower.class);

    public TemporaryDamageModifierPower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);

        Initialize(amount);
    }

    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type, AbstractCard card)
    {
        if (type == DamageInfo.DamageType.NORMAL && card != null && card.type == AbstractCard.CardType.ATTACK)
        {
            damage *= 1 + (amount / 100f);
        }

        return super.atDamageGive(damage, type, card);
    }

    @Override
    public void onAfterCardPlayed(AbstractCard usedCard)
    {
        super.onAfterCardPlayed(usedCard);

        if (usedCard.type == AbstractCard.CardType.ATTACK)
        {
            RemovePower();
        }
    }

    @Override
    public void atEndOfTurn(boolean isPlayer)
    {
        RemovePower();
    }
}
