package pinacolada.interfaces.subscribers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;

public interface OnDamageActionSubscriber
{
    void OnDamageAction(AbstractGameAction action, AbstractCreature target, DamageInfo info, AbstractGameAction.AttackEffect effect);
}
