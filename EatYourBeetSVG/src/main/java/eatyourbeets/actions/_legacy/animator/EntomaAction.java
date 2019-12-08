package eatyourbeets.actions._legacy.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.cards.animator.Entoma;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.utilities.GameUtilities;

public class EntomaAction extends AbstractGameAction
{
    private final Entoma entoma;

    public EntomaAction(Entoma entoma)
    {
        this.entoma = entoma;
        this.actionType = ActionType.SPECIAL;
        this.duration = 0.1F;
    }

    public void update()
    {
        if (EffectHistory.TryActivateLimited(entoma.cardID))
        {
            AbstractDungeon.player.increaseMaxHp(2, false);
            for (AbstractCard c : GameUtilities.GetAllInstances(entoma))
            {
                c.upgrade();
            }
        }

        this.isDone = true;
    }
}
