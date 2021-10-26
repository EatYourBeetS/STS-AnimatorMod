package eatyourbeets.interfaces.subscribers;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;

public interface OnDamageGiveSubscriber {
    float OnDamageGive(AbstractCreature target, DamageInfo.DamageType type, float damage, AbstractCard card);
}
