package eatyourbeets.actions.cardManipulation;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import eatyourbeets.actions.EYBActionWithCallback;
import eatyourbeets.cards.base.modifiers.CostModifiers;
import eatyourbeets.interfaces.delegates.FuncT1;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.GenericCondition;
import eatyourbeets.utilities.RandomizedList;

import java.util.List;

public class MotivateAction extends EYBActionWithCallback<AbstractCard>
{
    public static String ID = GR.CreateID("eyb", MotivateAction.class.getName());

    protected GenericCondition<AbstractCard> filter;
    protected boolean motivateZeroCost = true;
    protected boolean costReduced = false;
    protected AbstractCard card;
    protected CardGroup group;
    protected Integer effectIndex;

    public MotivateAction(int amount)
    {
        this(null, amount);
    }

    public MotivateAction(AbstractCard card, int amount)
    {
        super(ActionType.CARD_MANIPULATION, Settings.ACTION_DUR_FASTER);

        this.card = card;

        Initialize(amount);
    }

    public MotivateAction MotivateZeroCost(boolean value)
    {
        this.motivateZeroCost = value;

        return this;
    }

    public MotivateAction SetFilter(FuncT1<Boolean, AbstractCard> filter)
    {
        this.filter = GenericCondition.FromT1(filter);

        return this;
    }

    public MotivateAction SetGroup(List<AbstractCard> cards)
    {
        this.group = GameUtilities.CreateCardGroup(cards);

        return this;
    }

    public MotivateAction SetGroup(CardGroup group)
    {
        this.group = group;

        return this;
    }

    public MotivateAction ShowEffect(boolean show, int xOffsetIndex)
    {
        this.effectIndex = show ? xOffsetIndex : null;

        return this;
    }

    @Override
    protected void FirstUpdate()
    {
        if (amount == 0)
        {
            Complete(null);
        }

        if (card == null)
        {
            RandomizedList<AbstractCard> betterPossible = new RandomizedList<>();
            RandomizedList<AbstractCard> possible = new RandomizedList<>();

            if (group == null)
            {
                group = player.hand;
            }

            for (AbstractCard c : group.group)
            {
                if (filter == null || filter.Check(c))
                {
                    if (c.costForTurn > 0)
                    {
                        betterPossible.Add(c);
                    }
                    else if (c.cost > 0)
                    {
                        possible.Add(c);
                    }
                }
            }

            if (betterPossible.Size() > 0)
            {
                card = betterPossible.Retrieve(rng);
            }
            else if (motivateZeroCost && possible.Size() > 0)
            {
                card = possible.Retrieve(rng);
            }
        }

        if (card == null)
        {
            Complete(null);
            return;
        }

        final boolean zeroCost = card.costForTurn <= 0;
        if (zeroCost && !motivateZeroCost)
        {
            Complete(null);
            return;
        }

        CostModifiers.For(card).Add(ID, -amount);
        GameUtilities.TriggerWhenPlayed(card, c -> CostModifiers.For(c).Remove(ID, false));

        if (zeroCost)
        {
            GameUtilities.Flash(card, Color.GOLD, true);
            Complete(card);
        }
        else
        {
            ShowEffect();
        }
    }

    protected void ShowEffect()
    {
        if (group == null || group.type == CardGroup.CardGroupType.HAND || effectIndex == null)
        {
            GameUtilities.Flash(card, Color.GOLD, true);
            return;
        }

        final float offsetX;
        final float offsetY = (Settings.HEIGHT * 0.33f) + (effectIndex * AbstractCard.IMG_HEIGHT * 0.1f);
        if (group.type == CardGroup.CardGroupType.DRAW_PILE)
        {
            offsetX = (Settings.WIDTH * 0.12f) + (effectIndex * AbstractCard.IMG_WIDTH * 0.4f);
        }
        else if (group.type == CardGroup.CardGroupType.DISCARD_PILE || group.type == CardGroup.CardGroupType.EXHAUST_PILE)
        {
            offsetX = (Settings.WIDTH * 0.78f) - (effectIndex * AbstractCard.IMG_WIDTH * 0.4f);
        }
        else
        {
            offsetX = (Settings.WIDTH * 0.4f) + (effectIndex * AbstractCard.IMG_WIDTH * 0.4f);
        }

        GameEffects.TopLevelList.ShowCardBriefly(card.makeStatEquivalentCopy(), offsetX, offsetY);
    }

    @Override
    protected void UpdateInternal(float deltaTime)
    {
        if (TickDuration(deltaTime))
        {
            Complete(card);
        }
    }
}