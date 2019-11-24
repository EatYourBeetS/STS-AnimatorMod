package eatyourbeets.cards;

import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public abstract class AnimatorCard_Curse extends AnimatorCard
{
    protected AnimatorCard_Curse(String id, int cost, CardRarity rarity, CardTarget target)
    {
        super(id, cost, CardType.CURSE, CardColor.CURSE, rarity, target);
    }

    @Override
    public void triggerOnEndOfTurnForPlayingCard()
    {
        this.dontTriggerOnUseCard = true;
        AbstractDungeon.actionManager.cardQueue.add(new CardQueueItem(this, true));
    }

    @Override
    public boolean canUpgrade()
    {
        return false;
    }

    @Override
    public void upgrade()
    {

    }
}