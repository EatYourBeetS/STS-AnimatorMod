package eatyourbeets.cards.base;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public abstract class AnimatorCard_Status extends AnimatorCard
{
    protected boolean playAtEndOfTurn;

    protected AnimatorCard_Status(EYBCardData data, boolean playAtEndOfTurn)
    {
        super(data);

        this.playAtEndOfTurn = playAtEndOfTurn;
    }

    @Override
    public void triggerOnEndOfTurnForPlayingCard()
    {
        if (playAtEndOfTurn)
        {
            dontTriggerOnUseCard = true;

            AbstractDungeon.actionManager.cardQueue.add(new CardQueueItem(this, true));
        }
    }

    @Override
    public boolean canPlay(AbstractCard card)
    {
        if (card == this)
        {
            return true;
        }

        return super.canPlay(card);
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m)
    {
        return this.cardPlayable(m) && this.hasEnoughEnergy();
    }

    @Override
    public boolean canUpgrade()
    {
        return false;
    }
}