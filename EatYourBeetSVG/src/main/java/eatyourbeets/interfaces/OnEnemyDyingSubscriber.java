package eatyourbeets.interfaces;

import com.megacrit.cardcrawl.monsters.AbstractMonster;

public interface OnEnemyDyingSubscriber
{
    void OnEnemyDying(AbstractMonster monster, boolean triggerRelics);
}
