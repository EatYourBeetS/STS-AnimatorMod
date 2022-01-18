package pinacolada.interfaces.subscribers;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;

public interface OnCardMovedSubscriber
{
    void OnCardMoved(AbstractCard card, CardGroup source, CardGroup destination);
}
