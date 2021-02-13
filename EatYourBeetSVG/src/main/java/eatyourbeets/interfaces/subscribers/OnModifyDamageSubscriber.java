package eatyourbeets.interfaces.subscribers;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;

public interface OnModifyDamageSubscriber
{
    int OnModifyDamage(AbstractCreature target, DamageInfo info, int damage);
} 