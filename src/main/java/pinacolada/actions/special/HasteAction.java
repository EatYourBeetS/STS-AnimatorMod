package pinacolada.actions.special;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import eatyourbeets.actions.EYBAction;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLActions;

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
        if (card.hasTag(GR.Enums.CardTags.HASTE))
        {
            PCLActions.Top.Draw(1);
            PCLActions.Top.Flash(card);
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

        isDone = true;
    }
}
