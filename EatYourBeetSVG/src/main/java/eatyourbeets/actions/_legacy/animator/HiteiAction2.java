package eatyourbeets.actions._legacy.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import eatyourbeets.utilities.GameActionsHelper_Legacy;
import eatyourbeets.utilities.RandomizedList;

public class HiteiAction2 extends AbstractGameAction
{
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("ExhaustAction");
    private final AbstractPlayer player;
    private final int cards;

    public HiteiAction2(int cards)
    {
        this.cards = cards;
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
            for (int i = 0; i < cards; i++)
            {
                if (randomizedList.Count() > 0)
                {
                    group.addToTop(randomizedList.Retrieve(AbstractDungeon.cardRandomRng));
                }
            }

            if (group.size() > 0)
            {
                AbstractDungeon.gridSelectScreen.open(group, 1, uiStrings.TEXT[0], false, false, false, false);
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
                GameActionsHelper_Legacy.AddToTop(new ExhaustSpecificCardAction(card, group));
            }

            AbstractDungeon.gridSelectScreen.selectedCards.clear();
        }

        this.tickDuration();
    }
}
