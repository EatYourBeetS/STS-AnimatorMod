package eatyourbeets.interfaces.subscribers;

import com.megacrit.cardcrawl.cards.DamageInfo;

public interface OnLoseHPSubscriber
{
    void OnLoseHP(DamageInfo info, int amount);
}
