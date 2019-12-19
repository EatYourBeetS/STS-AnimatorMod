package eatyourbeets.actions.handSelection;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.actions.EYBActionWithCallback;
import eatyourbeets.utilities.RandomizedList;

import java.util.ArrayList;
import java.util.function.Predicate;

public class SelectFromHand extends EYBActionWithCallback<ArrayList<AbstractCard>>
{
    protected final ArrayList<AbstractCard> excluded = new ArrayList<>();
    protected final ArrayList<AbstractCard> selectedCards = new ArrayList<>();
    protected Predicate<AbstractCard> filter;
    protected boolean isRandom;
    protected boolean canPickLower;
    protected boolean anyNumber;
    protected boolean canPickZero;
    protected boolean forTransform;
    protected boolean forUpgrade;
    protected boolean upTo;

    public SelectFromHand(String sourceName, int amount, boolean isRandom)
    {
        this(ActionType.CARD_MANIPULATION, sourceName, amount, isRandom);
    }

    public SelectFromHand(ActionType type, String sourceName, int amount, boolean isRandom)
    {
        super(type);

        Initialize(amount, sourceName);

        this.isRandom = isRandom;
    }

    public SelectFromHand SetMessage(String message)
    {
        this.message = message;

        return this;
    }

    public SelectFromHand SetFilter(Predicate<AbstractCard> filter)
    {
        this.filter = filter;

        return this;
    }

    public SelectFromHand SetOptions(boolean anyNumber, boolean canPickZero, boolean canPickLower)
    {
        return SetOptions(anyNumber, canPickZero, canPickLower, false, false, false);
    }

    public SelectFromHand SetOptions(boolean anyNumber, boolean canPickZero, boolean canPickLower, boolean forTransform, boolean forUpgrade, boolean upTo)
    {
        this.anyNumber = anyNumber;
        this.canPickLower = canPickLower;
        this.canPickZero = canPickZero;
        this.forTransform = forTransform;
        this.forUpgrade = forUpgrade;
        this.upTo = upTo;

        return this;
    }

    @Override
    protected void FirstUpdate()
    {
        CardGroup cardSource;
        if (filter != null)
        {
            cardSource = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            for (AbstractCard card : player.hand.group)
            {
                if (filter.test(card))
                {
                    cardSource.addToTop(card);
                }
                else
                {
                    excluded.add(card);
                }
            }
        }
        else
        {
            cardSource = player.hand;
        }

        if (forUpgrade)
        {
            for (AbstractCard card : cardSource.group)
            {
                if (!card.canUpgrade())
                {
                    cardSource.removeCard(card);
                    excluded.add(card);
                }
            }
        }

        int size = cardSource.size();
        if (size < amount && !canPickLower)
        {
            Complete(); // Do not trigger callback
            return;
        }
        else if (amount == 0 || size == 0)
        {
            Complete(selectedCards);
            return;
        }
        else if (size <= amount && !anyNumber)
        {
            selectedCards.addAll(cardSource.group);
            Complete(selectedCards);
            return;
        }

        if (isRandom)
        {
            RandomizedList<AbstractCard> list = new RandomizedList<>();
            list.AddAll(cardSource.group);

            int max = Math.min(amount, list.Count());
            for (int i = 0; i < max; i++)
            {
                selectedCards.add(list.Retrieve(AbstractDungeon.cardRandomRng));
            }

            Complete(selectedCards);
            return;
        }

        if (cardSource.type == CardGroup.CardGroupType.UNSPECIFIED)
        {
            player.hand.group.clear();
            player.hand.group.addAll(cardSource.group);
        }

        AbstractDungeon.handCardSelectScreen.open(CreateMessage(), amount, anyNumber, canPickZero, forTransform, forUpgrade, upTo);
    }

    @Override
    protected void UpdateInternal()
    {
        if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved)
        {
            for (AbstractCard card : AbstractDungeon.handCardSelectScreen.selectedCards.group)
            {
                player.hand.addToTop(card);
                selectedCards.add(card);
            }

            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
            AbstractDungeon.handCardSelectScreen.selectedCards.group.clear();
        }

        tickDuration();
        
        if (isDone)
        {
            Complete(selectedCards);
        }
    }

    @Override
    protected void Complete()
    {
        super.Complete();

        player.hand.group.addAll(excluded);
        player.hand.refreshHandLayout();
        excluded.clear();
    }
}
