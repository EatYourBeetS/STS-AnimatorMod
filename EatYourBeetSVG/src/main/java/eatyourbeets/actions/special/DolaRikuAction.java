package eatyourbeets.actions.special;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import eatyourbeets.actions.EYBAction;
import eatyourbeets.cards.animator.DolaRiku;
import eatyourbeets.resources.AbstractResources;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameActionsHelper_Legacy;
import eatyourbeets.utilities.RandomizedList;

import java.util.ArrayList;

public class DolaRikuAction extends EYBAction
{
    public DolaRikuAction(AbstractCard exhausted, int choices)
    {
        super(ActionType.CARD_MANIPULATION);

        Initialize(choices, AbstractResources.GetCardStrings(DolaRiku.ID).NAME);

        card = exhausted;
    }

    @Override
    protected void FirstUpdate()
    {
        boolean status = card.type == AbstractCard.CardType.STATUS;
        boolean curse = card.type == AbstractCard.CardType.CURSE;
        boolean special = card.rarity == AbstractCard.CardRarity.SPECIAL;

        AbstractCard.CardColor mainColor;
        if (card.color == AbstractCard.CardColor.COLORLESS)
        {
            mainColor = player.getCardColor();
        }
        else
        {
            mainColor = card.color;
        }

        RandomizedList<AbstractCard> sameRarity = new RandomizedList<>();
        ArrayList<AbstractCard> allCards = CardLibrary.getAllCards();
        for (AbstractCard c : allCards)
        {
            if (c.color == AbstractCard.CardColor.COLORLESS || c.color == AbstractCard.CardColor.CURSE || c.color == mainColor)
            {
                if (!c.cardID.equals(card.cardID) && !c.tags.contains(AbstractCard.CardTags.HEALING))
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
                    else if (special || c.rarity == card.rarity)
                    {
                        AbstractCard toAdd = c.makeCopy();
                        if (card.upgraded && toAdd.canUpgrade())
                        {
                            toAdd.upgrade();
                        }
                        sameRarity.Add(toAdd);
                    }
                }
            }
        }

        CardGroup cardGroup = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

        int max = Math.min(amount, sameRarity.Count());
        for (int i = 0; i < max; i++)
        {
            cardGroup.group.add(sameRarity.Retrieve(AbstractDungeon.cardRandomRng));
        }

        AbstractDungeon.gridSelectScreen.open(cardGroup, 1, CreateMessage(), false);
    }

    @Override
    protected void UpdateInternal()
    {
        if (AbstractDungeon.gridSelectScreen.selectedCards.size() > 0)
        {
            AbstractCard card = AbstractDungeon.gridSelectScreen.selectedCards.get(0);

            card.modifyCostForCombat(-1);
            GameActions.Bottom.MakeCardInHand(card, false, false);

            AbstractDungeon.gridSelectScreen.selectedCards.clear();
        }

        tickDuration();
    }

    @Override
    protected String CreateMessage()
    {
        message = CardRewardScreen.TEXT[1];

        return super.CreateMessage();
    }
}
