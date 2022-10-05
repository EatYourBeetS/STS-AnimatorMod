package eatyourbeets.actions.cardManipulation;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import eatyourbeets.actions.EYBActionWithCallback;
import eatyourbeets.cards.base.modifiers.CostModifiers;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

public class MotivateCard extends EYBActionWithCallback<AbstractCard>
{
    public static String ID = GR.Common.CreateID(MotivateCard.class.getName());

    protected CardGroup group;
    protected Integer effectIndex;

    public MotivateCard(AbstractCard card, int amount)
    {
        super(ActionType.CARD_MANIPULATION, Settings.ACTION_DUR_FASTER);

        this.card = card;
        this.group = player.hand;

        Initialize(amount);
    }

    public MotivateCard SetGroup(CardGroup group)
    {
        this.group = group;

        return this;
    }

    public MotivateCard ShowEffect(boolean show, int xOffsetIndex)
    {
        this.effectIndex = show ? xOffsetIndex : null;

        return this;
    }

    @Override
    protected void FirstUpdate()
    {
        if (amount <= 0 || card == null)
        {
            Complete();
            return;
        }

        final boolean zeroCost = card.costForTurn <= 0;
        CostModifiers.For(card).Add(ID, -amount);
        GameUtilities.TriggerWhenPlayed(card, c -> CostModifiers.For(c).Remove(ID, false));
        PlayEffect(zeroCost || group == null);
    }

    @Override
    protected void UpdateInternal(float deltaTime)
    {
        if (TickDuration(deltaTime))
        {
            Complete(card);
        }
    }

    protected void PlayEffect(boolean isFast)
    {
        if (isFast)
        {
            if (player.hand.contains(card))
            {
                GameUtilities.Flash(card, Color.GOLD, true);
            }

            Complete(card);
            return;
        }

        if (group.type == CardGroup.CardGroupType.HAND || effectIndex == null)
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
}