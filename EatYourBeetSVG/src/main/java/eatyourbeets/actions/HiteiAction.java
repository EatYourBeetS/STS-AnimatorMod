package eatyourbeets.actions;

import com.evacipated.cardcrawl.mod.stslib.actions.common.FetchAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.Utilities;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.misc.RandomizedList;

public class HiteiAction extends AnimatorAction
{
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("ExhaustAction");
    private final AbstractPlayer player;

    public HiteiAction()
    {
        this.player = AbstractDungeon.player;
        this.setValues(this.player, AbstractDungeon.player, this.amount);
        this.duration = Settings.ACTION_DUR_FAST;
        this.actionType = ActionType.EXHAUST;
    }

    public void update()
    {
        if (this.duration == Settings.ACTION_DUR_FAST)
        {
            RandomizedList<AbstractCard> randomizedList = new RandomizedList<>();
            randomizedList.AddAll(player.drawPile.group);
            randomizedList.AddAll(player.discardPile.group);

            CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            for (int i = 0; i < 3; i++)
            {
                if (randomizedList.Count() > 0)
                {
                    group.addToTop(randomizedList.Retrieve(AbstractDungeon.miscRng));
                }
            }

            if (group.size() > 0)
            {
                AbstractDungeon.gridSelectScreen.open(group, 1, false, uiStrings.TEXT[0]);
            }
            else
            {
                this.isDone = true;
                return;
            }
        }

        if (AbstractDungeon.gridSelectScreen.selectedCards.size() > 0)
        {
            AbstractCard card = AbstractDungeon.gridSelectScreen.selectedCards.get(0);

            CardGroup group = null;
            if (player.drawPile.contains(card))
            {
                group = player.drawPile;
            }
            else if (player.discardPile.contains(card))
            {
                group = player.discardPile;
            }
            else if (player.hand.contains(card))
            {
                group = player.hand;
            }

            if (group != null)
            {
                GameActionsHelper.AddToTop(new ExhaustSpecificCardAction(card, group));
            }

            AbstractDungeon.gridSelectScreen.selectedCards.clear();
        }

        this.tickDuration();
    }
}
