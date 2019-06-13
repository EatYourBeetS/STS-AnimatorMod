package eatyourbeets.actions.animator;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import eatyourbeets.actions.common.ChooseFromPileAction;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.utilities.RandomizedList;

import java.util.ArrayList;

public class DolaRikuAction extends AnimatorAction
{
    private static final String discoveryMessage = CardCrawlGame.languagePack.getUIString("CardRewardScreen").TEXT[1];
    private static final String[] TEXT = CardCrawlGame.languagePack.getUIString("ExhaustAction").TEXT;
    private final AbstractPlayer player;
    private final int choices;

    public DolaRikuAction(AbstractCreature target, int choices)
    {
        this.target = target;
        this.choices = choices;
        this.player = (AbstractPlayer)target;
        this.duration = Settings.ACTION_DUR_FAST;
        this.actionType = ActionType.CARD_MANIPULATION;
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
                AbstractDungeon.handCardSelectScreen.open(TEXT[0], 1, false, false);
                this.tickDuration();
            }

            return;
        }

        if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved)
        {
            AbstractCard selectedCard = AbstractDungeon.handCardSelectScreen.selectedCards.getBottomCard();
            player.hand.moveToExhaustPile(selectedCard);

            boolean status = selectedCard.type == AbstractCard.CardType.STATUS;
            boolean curse = selectedCard.type == AbstractCard.CardType.CURSE;
            boolean special = selectedCard.rarity == AbstractCard.CardRarity.SPECIAL;

            AbstractCard.CardColor mainColor;
            if (selectedCard.color == AbstractCard.CardColor.COLORLESS)
            {
                mainColor = player.getCardColor();
            }
            else
            {
                mainColor = selectedCard.color;
            }

            RandomizedList<AbstractCard> sameRarity = new RandomizedList<>();
            ArrayList<AbstractCard> allCards = CardLibrary.getAllCards();
            for (AbstractCard c : allCards)
            {
                if (c.color == AbstractCard.CardColor.COLORLESS || c.color == AbstractCard.CardColor.CURSE || c.color == mainColor)
                {
                    if (!c.cardID.equals(selectedCard.cardID) && !c.tags.contains(AbstractCard.CardTags.HEALING))
                    {
                        if (c.type == AbstractCard.CardType.CURSE)
                        {
                            if (curse)
                            {
                                sameRarity.Add(c.makeCopy());
                            }
                        }
                        else if (c.type == AbstractCard.CardType.STATUS)
                        {
                            if (status)
                            {
                                sameRarity.Add(c.makeCopy());
                            }
                        }
                        else if (special || c.rarity == selectedCard.rarity)
                        {
                            AbstractCard toAdd = c.makeCopy();
                            if (selectedCard.upgraded && toAdd.canUpgrade())
                            {
                                toAdd.upgrade();
                            }
                            sameRarity.Add(toAdd);
                        }
                    }
                }
            }

            CardGroup cardGroup = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

            int max = Math.min(choices, sameRarity.Count());
            for (int i = 0; i < max; i++)
            {
                cardGroup.group.add(sameRarity.Retrieve(AbstractDungeon.miscRng));
            }

            GameActionsHelper.AddToTop(new ChooseFromPileAction(1, false, cardGroup, DolaRikuAction::OnCardChosen, this, discoveryMessage));

            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;

            this.isDone = true;
        }

        this.tickDuration();
    }

    public static void OnCardChosen(Object state, ArrayList<AbstractCard> cards)
    {
        if (state != null && cards != null && cards.size() == 1)
        {
            AbstractCard card = cards.get(0);
            card.modifyCostForCombat(-1);
            GameActionsHelper.AddToBottom(new MakeTempCardInHandAction(card));
        }
    }
}
