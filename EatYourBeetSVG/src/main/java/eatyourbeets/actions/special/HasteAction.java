package eatyourbeets.actions.special;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import eatyourbeets.actions.EYBAction;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.utilities.GameActions;

public class HasteAction extends EYBAction
{
    public HasteAction(EYBCard card)
    {
        super(ActionType.SPECIAL);

        this.isRealtime = true;
        this.card = card;

        Initialize(1);
    }

    @Override
    protected void FirstUpdate()
    {
        if (!((EYBCard)card).haste)
        {
            isDone = true;
        }

        GameActions.Top.Draw(1);
        GameActions.Top.Flash(card);
    }

    @Override
    protected void UpdateInternal()
    {
        super.UpdateInternal();

        if (isDone)
        {
            CardCrawlGame.sound.playA("POWER_FLIGHT",  MathUtils.random(0.3f, 0.4f));
            ((EYBCard)card).haste = false;
        }
    }
}
