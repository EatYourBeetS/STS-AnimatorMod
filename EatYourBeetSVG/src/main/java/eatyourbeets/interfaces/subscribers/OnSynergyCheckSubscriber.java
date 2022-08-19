package eatyourbeets.interfaces.subscribers;

import com.megacrit.cardcrawl.cards.AbstractCard;

public interface OnSynergyCheckSubscriber
{
    boolean OnSynergyCheck(AbstractCard a, AbstractCard b);
}