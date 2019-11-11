package eatyourbeets.actions.animator;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.cards.animator.Entoma;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.ui.EffectHistory;

public class EntomaAction extends AnimatorAction
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
            for (AbstractCard c : entoma.GetAllInstances())
            {
                c.upgrade();
            }
        }

        this.isDone = true;
    }
}
