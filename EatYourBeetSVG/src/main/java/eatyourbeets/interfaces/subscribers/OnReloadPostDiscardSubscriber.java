package eatyourbeets.interfaces.subscribers;

import com.megacrit.cardcrawl.cards.AbstractCard;

import java.util.ArrayList;

public interface OnReloadPostDiscardSubscriber
{
    void OnReloadPostDiscard(ArrayList<AbstractCard> cards);
} 
