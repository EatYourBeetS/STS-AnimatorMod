package eatyourbeets.interfaces.subscribers;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public interface OnPlayCardSubscriber
{
    void OnPlayCard(AbstractCard card, AbstractMonster monster);
}
