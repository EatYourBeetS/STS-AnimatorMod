package eatyourbeets.interfaces.subscribers;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;

public interface OnRawDamageReceived
{
    int OnRawDamageReceived(AbstractCreature target, DamageInfo info, int damage);
} 