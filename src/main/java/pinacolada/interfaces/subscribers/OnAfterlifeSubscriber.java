package pinacolada.interfaces.subscribers;

import com.megacrit.cardcrawl.cards.AbstractCard;

import java.util.ArrayList;

public interface OnAfterlifeSubscriber
{
    void OnAfterlife(AbstractCard playedCard, ArrayList<AbstractCard> fuelCards);
}
