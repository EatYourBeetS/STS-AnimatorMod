package eatyourbeets.interfaces.subscribers;

import com.megacrit.cardcrawl.cards.AbstractCard;

public interface OnCostChangedSubscriber
{
    void OnCostChanged(AbstractCard card, int originalCost, int newCost);
}