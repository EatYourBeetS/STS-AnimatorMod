package eatyourbeets.actions.orbs;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.actions.animator.AnimatorAction;
import eatyourbeets.orbs.Aether;

public class AirOrbEvokeAction extends AnimatorAction
{
    private final Aether wind;

    public AirOrbEvokeAction(Aether wind)
    {
        this.actionType = ActionType.DAMAGE;
        this.duration = 0.1F;
        this.wind = wind;
    }

    public void update()
    {
        GameActionsHelper.DrawCard(AbstractDungeon.player, wind.evokeAmount);

        this.isDone = true;
    }
}