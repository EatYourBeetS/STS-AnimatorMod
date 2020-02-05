package eatyourbeets.actions.animator;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import eatyourbeets.actions.EYBAction;
import eatyourbeets.cards.animator.series.NoGameNoLife.DolaRiku;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.RandomizedList;

import java.util.ArrayList;

public class DolaRikuAction extends EYBAction
{
    public DolaRikuAction(AbstractCard exhausted, int choices)
    {
        super(ActionType.CARD_MANIPULATION);

        Initialize(choices, DolaRiku.DATA.Strings.NAME);

        card = exhausted;
    }

    @Override
    protected void FirstUpdate()
    {
        boolean status = card.type == AbstractCard.CardType.STATUS;
        boolean curse = card.type == AbstractCard.CardType.CURSE;
        boolean special = card.rarity == AbstractCard.CardRarity.SPECIAL;
        boolean colorless = card.color == AbstractCard.CardColor.COLORLESS;

        AbstractCard.CardColor mainColor;
        if (colorless)
        {
            mainColor = player.getCardColor();
        }
        else
        {
            mainColor = card.color;
        }

        RandomizedList<AbstractCard> sameRarity = new RandomizedList<>();
        ArrayList<AbstractCard> allCards = CardLibrary.getAllCards();
        for (AbstractCard temp : allCards)
        {
            if (temp.cardID.equals(card.cardID) || temp.tags.contains(AbstractCard.CardTags.HEALING))
            {
                continue;
            }

            if (temp.type == AbstractCard.CardType.CURSE)
            {
                if (curse)
                {
                    sameRarity.Add(temp);
                }
            }
            else if (temp.type == AbstractCard.CardType.STATUS)
            {
                if (status)
                {
                    sameRarity.Add(temp);
                }
            }
            else if (special || temp.rarity == card.rarity)
            {
                if (temp.color == mainColor || temp.color == AbstractCard.CardColor.COLORLESS)
                {
                    sameRarity.Add(temp);
                }
            }
        }

        CardGroup cardGroup = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

        int max = Math.min(amount, sameRarity.Count());
        for (int i = 0; i < max; i++)
        {
            AbstractCard toAdd = sameRarity.Retrieve(AbstractDungeon.cardRandomRng).makeCopy();
            if (card.upgraded && toAdd.canUpgrade())
            {
                toAdd.upgrade();
            }
            cardGroup.group.add(toAdd);
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
            GameActions.Bottom.MakeCardInHand(card);

            AbstractDungeon.gridSelectScreen.selectedCards.clear();
        }

        tickDuration();
    }

    @Override
    public String CreateMessage()
    {
        return super.CreateMessageInternal(CardRewardScreen.TEXT[1]);
    }
}
