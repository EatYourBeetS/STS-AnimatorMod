package eatyourbeets.interfaces.subscribers;

import com.megacrit.cardcrawl.cards.AbstractCard;

public interface OnCostRefreshSubscriber
{
    void OnCostRefresh(AbstractCard card);
}