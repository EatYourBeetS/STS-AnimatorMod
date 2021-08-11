package eatyourbeets.actions.cardManipulation;

import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import eatyourbeets.actions.EYBActionWithCallback;
import eatyourbeets.resources.GR;

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
            Complete();
            return;
        }

        CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        if (amount != -1)
        {
            for (int i = 0; i < Math.min(amount, player.drawPile.size()); ++i)
            {
                group.addToTop(player.drawPile.group.get(player.drawPile.size() - i - 1));
            }
        }
        else
        {
            for (AbstractCard c : player.drawPile.group)
            {
                group.addToBottom(c);
            }
        }

        AbstractDungeon.gridSelectScreen.open(group, amount, true, GR.Common.Strings.GridSelection.Scry);
    }

    @Override
    protected void UpdateInternal(float deltaTime)
    {
        if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty())
        {
            for (AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards)
            {
                c.triggerOnManualDiscard();
                player.drawPile.moveToDiscardPile(c);
                selectedCards.add(c);
            }
            
            GameActionManager.incrementDiscard(false);
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
        }

        for (AbstractCard c : player.discardPile.group)
        {
            c.triggerOnScry();
        }

        if (TickDuration(deltaTime))
        {
            Complete(selectedCards);
        }
    }
}

