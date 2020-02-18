package eatyourbeets.actions.animator;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import eatyourbeets.actions.EYBAction;
import eatyourbeets.cards.animator.series.Katanagatari.HigakiRinne;
import eatyourbeets.cards.animator.series.NoGameNoLife.DolaRiku;
import eatyourbeets.resources.GR;
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
        boolean special = !curse && card.rarity == AbstractCard.CardRarity.SPECIAL;
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
            if (temp.cardID.equals(card.cardID) || temp.tags.contains(AbstractCard.CardTags.HEALING) || temp.tags.contains(GR.Enums.CardTags.TEMPORARY))
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

        boolean addedRinne = false;
        int max = Math.min(amount, sameRarity.Size());
        for (int i = 0; i < max; i++)
        {
            AbstractCard toAdd = sameRarity.Retrieve(AbstractDungeon.cardRandomRng).makeCopy();
            if (card.upgraded && toAdd.canUpgrade())
            {
                toAdd.upgrade();
            }
            if (HigakiRinne.DATA.ID.equals(toAdd.cardID))
            {
                addedRinne = true;
            }
            cardGroup.group.add(toAdd);
        }

        if (!addedRinne && cardGroup.size() > 2 && AbstractDungeon.cardRandomRng.randomBoolean(0.05f))
        {
            cardGroup.group.remove(0);
            HigakiRinne rinne = new HigakiRinne();
            if (card.upgraded)
            {
                rinne.upgrade();
            }
            cardGroup.group.add(rinne);
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
