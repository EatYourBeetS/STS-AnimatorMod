package eatyourbeets.actions.Orbs;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.actions.AnimatorAction;
import eatyourbeets.orbs.Air;

public class AirOrbEvokeAction extends AnimatorAction
{
    private final Air wind;

    public AirOrbEvokeAction(Air wind)
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