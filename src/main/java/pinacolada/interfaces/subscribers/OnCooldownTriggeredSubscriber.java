package pinacolada.interfaces.subscribers;

import com.megacrit.cardcrawl.cards.AbstractCard;
import pinacolada.cards.base.PCLCardCooldown;

public interface OnCooldownTriggeredSubscriber
{
    boolean OnCooldownTriggered(AbstractCard card, PCLCardCooldown cooldown);
}
