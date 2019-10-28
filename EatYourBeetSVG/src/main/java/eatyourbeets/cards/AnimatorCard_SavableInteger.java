package eatyourbeets.cards;

import basemod.abstracts.CustomSavable;
import com.megacrit.cardcrawl.localization.CardStrings;

public abstract class AnimatorCard_SavableInteger extends AnimatorCard implements CustomSavable<Integer>
{
    protected AnimatorCard_SavableInteger(String id, int cost, CardType type, CardRarity rarity, CardTarget target)
    {
        super(id, cost, type, rarity, target);
    }

    protected AnimatorCard_SavableInteger(String id, int cost, CardType type, CardColor color, CardRarity rarity, CardTarget target)
    {
        super(id, cost, type, color, rarity, target);
    }

    protected AnimatorCard_SavableInteger(EYBCardData data, String id, String imagePath, int cost, CardType type, CardColor color, CardRarity rarity, CardTarget target)
    {
        super(data, id, imagePath, cost, type, color, rarity, target);
    }

    @Override
    public Integer onSave()
    {
        return this.secondaryValue;
    }

    @Override
    public void onLoad(Integer integer)
    {
        SetValue(integer);
        initializeDescription();
    }

    protected void SetValue(Integer integer)
    {
        this.baseSecondaryValue = integer != null ? integer : 0;
        this.secondaryValue = this.baseSecondaryValue;
    }
}
