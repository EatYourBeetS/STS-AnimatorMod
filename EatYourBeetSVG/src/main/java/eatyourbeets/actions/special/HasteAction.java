package eatyourbeets.actions.special;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import eatyourbeets.actions.EYBAction;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;

public class HasteAction extends EYBAction
{
    public HasteAction(AbstractCard card)
    {
        super(ActionType.SPECIAL);

        this.isRealtime = true;
        this.card = card;

        Initialize(1);
    }

    @Override
    protected void FirstUpdate()
    {
        if (card.hasTag(GR.Enums.CardTags.HASTE) || card.hasTag(GR.Enums.CardTags.HASTE_INFINITE))
        {
            GameActions.Top.Draw(1);
            GameActions.Top.Flash(card);
        }
        else
        {
            isDone = true;
        }
    }

    @Override
    protected void Complete()
    {
        if (card.hasTag(GR.Enums.CardTags.HASTE))
        {
            CardCrawlGame.sound.playA("POWER_FLIGHT", MathUtils.random(0.3f, 0.4f));
            card.tags.remove(GR.Enums.CardTags.HASTE);
        }
        else if (card.hasTag(GR.Enums.CardTags.HASTE_INFINITE)) {
            CardCrawlGame.sound.playA("POWER_FLIGHT", MathUtils.random(0.3f, 0.4f));
        }

        isDone = true;
    }
}
