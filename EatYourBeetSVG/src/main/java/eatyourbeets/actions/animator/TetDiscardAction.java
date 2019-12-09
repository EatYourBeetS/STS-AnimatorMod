package eatyourbeets.actions.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.FrozenEye;
import eatyourbeets.resources.Resources_Animator_Strings;
import eatyourbeets.utilities.JavaUtilities;

public class TetDiscardAction extends AbstractGameAction
{
    private static final String[] TEXT = Resources_Animator_Strings.TetAction.TEXT;

    public TetDiscardAction(int num)
    {
        this.amount = num;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    public void update()
    {
        if (this.duration == Settings.ACTION_DUR_FAST)
        {
            AbstractPlayer p = AbstractDungeon.player;

            int replaceNumber = Math.min(amount, p.drawPile.size());
            if (replaceNumber <= 0)
            {
                this.isDone = true;
                return;
            }

            String message = JavaUtilities.Format(TEXT[0], String.valueOf(replaceNumber));

            CardGroup tmp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

            if (p.hasRelic(FrozenEye.ID))
            {
                for (AbstractCard c : p.drawPile.group)
                {
                    tmp.addToBottom(c);
                }
            }
            else
            {
                for (AbstractCard c : p.drawPile.group)
                {
                    tmp.addToRandomSpot(c);
                }
            }

            AbstractDungeon.gridSelectScreen.open(tmp, replaceNumber, true, message);
        }
        else if (AbstractDungeon.gridSelectScreen.selectedCards.size() > 0)
        {
            AbstractPlayer p = AbstractDungeon.player;
            for (AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards)
            {
                AbstractDungeon.player.drawPile.moveToDiscardPile(c);
                c.triggerOnManualDiscard();
                GameActionManager.incrementDiscard(false);
            }

            AbstractDungeon.gridSelectScreen.selectedCards.clear();
        }

        tickDuration();
    }
}