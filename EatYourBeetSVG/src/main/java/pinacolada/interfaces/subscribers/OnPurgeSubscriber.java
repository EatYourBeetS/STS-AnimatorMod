package pinacolada.interfaces.subscribers;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;

public interface OnPurgeSubscriber
{
    void OnPurge(AbstractCard card, CardGroup source);
}
