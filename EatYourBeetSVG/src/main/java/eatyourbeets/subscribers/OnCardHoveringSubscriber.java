package eatyourbeets.subscribers;

import com.megacrit.cardcrawl.monsters.AbstractMonster;

public interface OnCardHoveringSubscriber
{
    void OnCardHovering(AbstractMonster hoveredMonster);
}
