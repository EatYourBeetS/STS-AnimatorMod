package eatyourbeets.actions;

import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.Utilities;

import java.util.ArrayList;

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
        if (p.drawPile.size() > 0 && p.hand.contains(target))
        {
            AbstractCard replacement = p.drawPile.getRandomCard(true);
            GameActionsHelper.AddToTop(new DrawSpecificCardAction(replacement));
            GameActionsHelper.AddToTop(new MoveSpecificCardAction(target, p.drawPile, p.hand, true));
            GameActionsHelper.AddToTop(new WaitAction(0.5f));
        }

        this.isDone = true;
    }
}
