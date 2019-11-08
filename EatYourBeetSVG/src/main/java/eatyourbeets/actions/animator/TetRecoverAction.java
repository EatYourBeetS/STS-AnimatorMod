package eatyourbeets.actions.animator;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.resources.Resources_Animator_Strings;
import eatyourbeets.utilities.Utilities;

public class TetRecoverAction extends AnimatorAction
{
    private static final String[] TEXT = Resources_Animator_Strings.TetAction.TEXT;

    public TetRecoverAction(int num)
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

            int replaceNumber = Math.min(amount, p.discardPile.size());
            if (replaceNumber <= 0)
            {
                this.isDone = true;
                return;
            }

            String message = Utilities.Format(TEXT[1], replaceNumber);

            AbstractDungeon.gridSelectScreen.open(p.discardPile, replaceNumber, true, message);
        }
        else if (AbstractDungeon.gridSelectScreen.selectedCards.size() > 0)
        {
            for (AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards)
            {
                AbstractDungeon.player.discardPile.moveToDeck(c, true);
            }

            AbstractDungeon.gridSelectScreen.selectedCards.clear();
        }

        tickDuration();
    }
}