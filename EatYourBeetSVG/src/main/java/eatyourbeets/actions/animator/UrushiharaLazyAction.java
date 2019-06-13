package eatyourbeets.actions.animator;

import com.megacrit.cardcrawl.actions.common.DiscardSpecificCardAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.actions.common.MoveSpecificCardAction;
import eatyourbeets.utilities.GameActionsHelper;

public class UrushiharaLazyAction extends AnimatorAction
{
    private final AbstractCard target;

    public UrushiharaLazyAction(AbstractCard target)
    {
        this.target = target;
        this.duration = Settings.ACTION_DUR_FAST;
        this.actionType = ActionType.CARD_MANIPULATION;
    }

    public void update()
    {
        AbstractPlayer p = AbstractDungeon.player;
        if (p.discardPile.size() > 0 && p.hand.contains(target))
        {
            AbstractCard replacement = p.discardPile.getRandomCard(true);
            GameActionsHelper.AddToTop(new MoveSpecificCardAction(replacement, p.hand, p.discardPile, false));
            GameActionsHelper.AddToTop(new DiscardSpecificCardAction(target, p.hand));
            GameActionsHelper.AddToTop(new WaitAction(0.5f));
        }

        this.isDone = true;
    }
}
