package eatyourbeets.actions.handSelection;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.actions.EYBActionWithCallback;
import eatyourbeets.interfaces.delegates.FuncT1;
import eatyourbeets.interfaces.delegates.FuncT2;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.GenericCondition;
import eatyourbeets.utilities.RandomizedList;

import java.util.ArrayList;

public class SelectFromHand extends EYBActionWithCallback<ArrayList<AbstractCard>>
{
    protected final ArrayList<AbstractCard> excluded = new ArrayList<>();
    protected final ArrayList<AbstractCard> selectedCards = new ArrayList<>();
    protected GenericCondition<AbstractCard> filter;
    protected boolean reAddCards;
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

        this.message = GR.Common.Strings.HandSelection.Activate;
        this.isRandom = isRandom;
    }

    public SelectFromHand SetMessage(String message)
    {
        this.message = message;

        return this;
    }

    public SelectFromHand SetFilter(FuncT1<Boolean, AbstractCard> filter)
    {
        this.filter = GenericCondition.FromT1(filter);

        return this;
    }

    public <S> SelectFromHand SetFilter(S state, FuncT2<Boolean, S, AbstractCard> filter)
    {
        this.filter = GenericCondition.FromT2(filter, state);

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
                if (filter.Check(card))
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
        else if (size <= amount && !canPickZero && !anyNumber)
        {
            selectedCards.addAll(cardSource.group);
            Complete(selectedCards);
            return;
        }

        if (isRandom)
        {
            RandomizedList<AbstractCard> list = new RandomizedList<>();
            list.AddAll(cardSource.group);

            int max = Math.min(amount, list.Size());
            for (int i = 0; i < max; i++)
            {
                selectedCards.add(list.Retrieve(rng));
            }

            Complete(selectedCards);
            return;
        }

        if (cardSource.type == CardGroup.CardGroupType.UNSPECIFIED)
        {
            player.hand.group.clear();
            player.hand.group.addAll(cardSource.group);
            reAddCards = true;
        }

        AbstractDungeon.handCardSelectScreen.open(UpdateMessage(), amount, anyNumber, canPickZero, forTransform, forUpgrade, upTo);
    }

    @Override
    protected void UpdateInternal(float deltaTime)
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

        if (TickDuration(deltaTime))
        {
            Complete(selectedCards);
        }
    }

    @Override
    protected void Complete()
    {
        super.Complete();

        if (reAddCards)
        {
            player.hand.group.addAll(excluded);
            excluded.clear();
        }

        GameUtilities.RefreshHandLayout();
    }
}
