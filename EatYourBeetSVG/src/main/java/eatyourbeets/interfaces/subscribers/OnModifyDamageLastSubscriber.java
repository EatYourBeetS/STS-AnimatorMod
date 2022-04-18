package eatyourbeets.interfaces.subscribers;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;

public interface OnModifyDamageLastSubscriber
{
    int OnModifyDamageLast(AbstractCreature target, DamageInfo info, int damage);
} 