package eatyourbeets.interfaces.subscribers;

import com.megacrit.cardcrawl.cards.AbstractCard;

public interface OnReloadPreDiscardSubscriber
{
    AbstractCard OnReloadPreDiscard(AbstractCard card);
} 
