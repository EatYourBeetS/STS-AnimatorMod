package eatyourbeets.actions._legacy.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import eatyourbeets.actions._legacy.common.DrawSpecificCardAction;
import eatyourbeets.utilities.GameActionsHelper_Legacy;
import eatyourbeets.utilities.RandomizedList;

public class HiteiAction extends AbstractGameAction
{
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("ExhaustAction");
    private final AbstractPlayer player;

    private final CardGroup cardGroup;

    public HiteiAction()
    {
        this.player = AbstractDungeon.player;
        this.setValues(this.player, AbstractDungeon.player, this.amount);
        this.duration = Settings.ACTION_DUR_FAST;
        this.actionType = ActionType.EXHAUST;
        this.cardGroup = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
    }

    public void update()
    {
        if (this.duration == Settings.ACTION_DUR_FAST)
        {
            RandomizedList<AbstractCard> randomizedList = new RandomizedList<>();
            randomizedList.AddAll(player.drawPile.group);
            randomizedList.AddAll(player.discardPile.group);

            cardGroup.clear();
            for (int i = 0; i < 2; i++)
            {
                if (randomizedList.Count() > 0)
                {
                    cardGroup.addToTop(randomizedList.Retrieve(AbstractDungeon.cardRandomRng));
                }
            }

            if (cardGroup.size() > 0)
            {
                AbstractDungeon.gridSelectScreen.open(cardGroup, 1, uiStrings.TEXT[0], false, false, false, false);
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

            GameActionsHelper_Legacy.ExhaustCard(card);

            for (AbstractCard c : cardGroup.group)
            {
                if (c != card)
                {
                    if (player.discardPile.contains(c))
                    {
                        player.discardPile.removeCard(c);
                        player.drawPile.addToTop(c);
                    }
                    GameActionsHelper_Legacy.AddToBottom(new DrawSpecificCardAction(c));
                    c.setCostForTurn(0);

                    break;
                }
            }

            AbstractDungeon.gridSelectScreen.selectedCards.clear();
        }

        this.tickDuration();
    }
}
