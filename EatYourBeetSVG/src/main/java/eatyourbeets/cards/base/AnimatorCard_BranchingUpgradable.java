package eatyourbeets.cards.base;

import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.BranchingUpgradesCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public abstract class AnimatorCard_BranchingUpgradable extends AnimatorCard implements BranchingUpgradesCard
{
    protected boolean playAtEndOfTurn;

    protected AnimatorCard_BranchingUpgradable(EYBCardData data, boolean playAtEndOfTurn)
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
    public boolean canUpgrade()
    {
        return false;
    }

    @Override
    public void upgrade()
    {

    }
}