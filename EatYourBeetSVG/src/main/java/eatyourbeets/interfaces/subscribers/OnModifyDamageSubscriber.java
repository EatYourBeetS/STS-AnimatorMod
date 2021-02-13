package eatyourbeets.interfaces.subscribers;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;

public interface OnModifyDamageSubscriber
{
    int OnModifyDamage(AbstractCreature creature, DamageInfo info, int damage);
} 
