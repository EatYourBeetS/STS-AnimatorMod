package eatyourbeets.interfaces.subscribers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public interface OnPlayerMinionActionSubscriber
{
    default boolean CanSummonMinion(boolean triggerEvents) { return true; }
    default void OnMinionSummon(AbstractCreature minion) { }
    default void OnMinionDeath(AbstractCreature minion) { }
    default void OnMinionActivation(AbstractCreature minion, boolean endOfTurn) { }
    default void OnMinionIntentChanged(AbstractCreature minion, AbstractMonster.Intent previous, AbstractMonster.Intent current) { }
}
