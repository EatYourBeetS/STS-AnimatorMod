package eatyourbeets.interfaces;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;

public interface OnAttackSubscriber
{
    void OnAttack(DamageInfo info, int damageAmount, AbstractCreature target);
}
