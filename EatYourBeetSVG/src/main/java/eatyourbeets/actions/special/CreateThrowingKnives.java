package eatyourbeets.actions.special;

import basemod.BaseMod;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToHandEffect;
import eatyourbeets.actions.EYBAction;
import eatyourbeets.cards.animator.ThrowingKnife;

public class CreateThrowingKnives extends EYBAction
{
    protected boolean upgraded;

    public CreateThrowingKnives(int amount)
    {
        super(ActionType.CARD_MANIPULATION);

        Initialize(amount);
    }

    public CreateThrowingKnives SetOptions(boolean upgraded)
    {
        this.upgraded = upgraded;

        return this;
    }

    @Override
    protected void FirstUpdate()
    {
        int max = Math.min(amount, BaseMod.MAX_HAND_SIZE - player.hand.size());
        for (int i = 0; i < max; i++)
        {
            AbstractCard card = ThrowingKnife.GetRandomCard();

            if (upgraded)
            {
                card.upgrade();
            }

            AbstractDungeon.effectList.add(new ShowCardAndAddToHandEffect(card));
        }

        Complete();
    }
}
