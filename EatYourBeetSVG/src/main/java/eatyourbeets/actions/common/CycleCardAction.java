package eatyourbeets.actions.common;

import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.resources.Resources_Animator;
import eatyourbeets.actions.animator.AnimatorAction;
import eatyourbeets.resources.Resources_Animator_Strings;
import eatyourbeets.utilities.GameActionsHelper;

public class CycleCardAction extends AnimatorAction
{
    private final AbstractPlayer player;

    public CycleCardAction(AbstractCreature target, int count)
    {
        this.target = target;
        this.amount = count;
        this.player = (AbstractPlayer)target;
        this.duration = Settings.ACTION_DUR_FAST;
        this.actionType = ActionType.DISCARD;
    }

    public void update()
    {
        if (this.duration == Settings.ACTION_DUR_FAST)
        {
            if (this.player.hand.size() == 0)
            {
                this.isDone = true;
            }
            else
            {
                String discardMessage = Resources_Animator_Strings.Actions.TEXT[1];
                AbstractDungeon.handCardSelectScreen.open(discardMessage, this.amount, true,true);
                GameActionsHelper.AddToBottom(new RefreshHandLayoutAction());
                this.tickDuration();
            }

            return;
        }

        if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved)
        {
            int discarded = 0;
            for (AbstractCard card : AbstractDungeon.handCardSelectScreen.selectedCards.group)
            {
                this.player.hand.moveToDiscardPile(card);
                card.triggerOnManualDiscard();
                GameActionManager.incrementDiscard(false);
                AbstractDungeon.player.hand.applyPowers();
                discarded += 1;
            }
            if (discarded > 0)
            {
                AbstractDungeon.actionManager.addToTop(new DrawCardAction(player, discarded));
            }

            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
        }

        this.tickDuration();
    }
}
