package eatyourbeets.interfaces.subscribers;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;

public interface OnApplyFocusSubscriber
{
    void OnAttack(DamageInfo info, int damageAmount, AbstractCreature target);
}
