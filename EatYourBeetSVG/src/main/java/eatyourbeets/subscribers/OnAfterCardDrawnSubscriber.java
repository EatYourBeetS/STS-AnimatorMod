package eatyourbeets.subscribers;

import com.megacrit.cardcrawl.cards.AbstractCard;

public interface OnAfterCardDrawnSubscriber
{
    void OnAfterCardDrawn(AbstractCard card);
}
