package eatyourbeets.actions.cardManipulation;

import com.megacrit.cardcrawl.actions.utility.ScryAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import eatyourbeets.actions.EYBActionWithCallback;

import java.util.ArrayList;

public class ScryWhichActuallyTriggersDiscard extends EYBActionWithCallback<ArrayList<AbstractCard>>
{
    protected final ArrayList<AbstractCard> selectedCards = new ArrayList<>();

    public ScryWhichActuallyTriggersDiscard(int amount)
    {
        super(ActionType.CARD_MANIPULATION, Settings.ACTION_DUR_FAST);

        Initialize(amount);
    }

    @Override
    protected void FirstUpdate()
    {
        if (AbstractDungeon.getMonsters().areMonstersBasicallyDead())
        {
            Complete();
            return;
        }

        for (AbstractPower p : player.powers)
        {
            p.onScry();
        }

        if (player.drawPile.isEmpty())
        {
            this.isDone = true;
            return;
        }

        CardGroup tmpGroup = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        if (this.amount != -1)
        {
            for (int i = 0; i < Math.min(this.amount, player.drawPile.size()); ++i)
            {
                tmpGroup.addToTop(player.drawPile.group.get(player.drawPile.size() - i - 1));
            }
        }
        else
        {

            for (AbstractCard c : player.drawPile.group)
            {
                tmpGroup.addToBottom(c);
            }
        }

        AbstractDungeon.gridSelectScreen.open(tmpGroup, this.amount, true, ScryAction.TEXT[0]);
    }

    @Override
    protected void UpdateInternal()
    {
        if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty())
        {
            for (AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards)
            {
                c.triggerOnManualDiscard();
                player.drawPile.moveToDiscardPile(c);
                selectedCards.add(c);
            }

            AbstractDungeon.gridSelectScreen.selectedCards.clear();
        }

        for (AbstractCard c : player.discardPile.group)
        {
            c.triggerOnScry();
        }

        tickDuration();

        if (isDone)
        {
            Complete(selectedCards);
        }
    }
}

