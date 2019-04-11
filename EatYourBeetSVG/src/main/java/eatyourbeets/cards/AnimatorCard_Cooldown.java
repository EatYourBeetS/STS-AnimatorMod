package eatyourbeets.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.helpers.GetAllInBattleInstances;

public abstract class AnimatorCard_Cooldown extends AnimatorCard// extends AnimatorCard_SavableInteger
{
    protected abstract int GetBaseCooldown();

    protected AnimatorCard_Cooldown(String id, int cost, CardType type, CardRarity rarity, CardTarget target)
    {
        super(id, cost, type, rarity, target);
        this.baseSecondaryValue = this.secondaryValue = GetBaseCooldown();
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        if (this.secondaryValue > 0)
        {
            ProgressCooldown();
        }
    }

    @Override
    public void applyPowers()
    {
        super.applyPowers();
        this.isSecondaryValueModified = (this.secondaryValue == 0);
        initializeDescription();
    }

    protected boolean ProgressCooldown()
    {
        boolean activate;
        int newValue;
        if (secondaryValue <= 0)
        {
            newValue = GetBaseCooldown();
            activate = true;
        }
        else
        {
            newValue = secondaryValue - 1;
            activate = false;
        }

        for (AbstractCard c : GetAllInBattleInstances.get(this.uuid))
        {
            AnimatorCard_Cooldown card = (AnimatorCard_Cooldown)c;
            card.baseSecondaryValue = card.secondaryValue = newValue;
            //card.applyPowers();
        }

        return activate;
    }
}
