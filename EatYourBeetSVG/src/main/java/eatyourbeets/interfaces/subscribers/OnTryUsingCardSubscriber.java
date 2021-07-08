package eatyourbeets.interfaces.subscribers;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public interface OnTryUsingCardSubscriber
{
    boolean OnTryUsingCard(AbstractCard card, AbstractPlayer p, AbstractMonster m, boolean canUse);
}
