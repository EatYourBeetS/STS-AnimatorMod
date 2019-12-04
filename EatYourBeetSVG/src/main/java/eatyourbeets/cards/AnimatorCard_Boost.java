package eatyourbeets.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JavaUtilities;

public abstract class AnimatorCard_Boost extends AnimatorCard
{
    protected abstract int GetBaseBoost();

    protected AnimatorCard_Boost(String id, int cost, CardType type, CardColor color, CardRarity rarity, CardTarget target)
    {
        super(id, cost, type, color, rarity, target);
    }

    protected AnimatorCard_Boost(String id, int cost, CardType type, CardRarity rarity, CardTarget target)
    {
        super(id, cost, type, rarity, target);
    }

    @Override
    protected void Initialize(int baseDamage, int baseBlock)
    {
        super.Initialize(baseDamage, baseBlock, -1, GetBaseBoost());
    }

    @Override
    protected void Initialize(int baseDamage, int baseBlock, int baseMagicNumber)
    {
        super.Initialize(baseDamage, baseBlock, baseMagicNumber, GetBaseBoost());
    }

    protected void ResetBoost()
    {
        this.secondaryValue = baseSecondaryValue;
        this.isSecondaryValueModified = false;
    }

    protected int GetCurrentBoost()
    {
        return this.secondaryValue;
    }

    public void upgradeBoost(int amount)
    {
        int currentBoost = GetCurrentBoost();

        this.baseSecondaryValue += amount;
        this.secondaryValue = currentBoost + amount;
        this.upgradedSecondaryValue = true;
        this.isSecondaryValueModified = (secondaryValue != baseSecondaryValue);
    }

    protected boolean ProgressBoost()
    {
        if (this.secondaryValue > 0)
        {
            int newValue = this.secondaryValue - 1;

            for (AbstractCard c : GameUtilities.GetAllInBattleInstances(this))
            {
                AnimatorCard_Boost card = JavaUtilities.SafeCast(c, AnimatorCard_Boost.class);
                if (card != null)
                {
                    card.secondaryValue = newValue;
                    card.isSecondaryValueModified = (secondaryValue != baseSecondaryValue);
                }
            }

            return true;
        }

        return false;
    }

    public void IncreaseBoost(int value)
    {
        this.secondaryValue = (GetCurrentBoost() + value);
        this.isSecondaryValueModified = (this.secondaryValue != this.baseSecondaryValue);
    }
}
