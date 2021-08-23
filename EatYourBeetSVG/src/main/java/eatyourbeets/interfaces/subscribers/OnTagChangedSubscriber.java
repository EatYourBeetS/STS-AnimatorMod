package eatyourbeets.interfaces.subscribers;

import com.megacrit.cardcrawl.cards.AbstractCard;

public interface OnTagChangedSubscriber
{
    void OnTagChanged(AbstractCard card, AbstractCard.CardTags tag, boolean value);
}