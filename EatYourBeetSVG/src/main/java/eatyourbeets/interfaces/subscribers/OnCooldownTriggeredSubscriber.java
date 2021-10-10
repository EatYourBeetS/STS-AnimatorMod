package eatyourbeets.interfaces.subscribers;

import com.megacrit.cardcrawl.cards.AbstractCard;
import eatyourbeets.cards.base.EYBCardCooldown;

public interface OnCooldownTriggeredSubscriber
{
    void OnCooldownTriggered(AbstractCard card, EYBCardCooldown cooldown);
}
