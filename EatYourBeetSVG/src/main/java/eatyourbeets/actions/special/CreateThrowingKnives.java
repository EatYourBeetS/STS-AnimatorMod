package eatyourbeets.actions.special;

import basemod.BaseMod;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToHandEffect;
import eatyourbeets.actions.EYBAction;
import eatyourbeets.actions._legacy.common.RefreshHandLayoutAction;
import eatyourbeets.cards.animator.ThrowingKnife;
import eatyourbeets.utilities.GameActions;

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
            GameActions.Top.MakeCard(ThrowingKnife.GetRandomCard(), player.hand)
            .SetOptions(upgraded, false);
        }

        Complete();
    }
}
