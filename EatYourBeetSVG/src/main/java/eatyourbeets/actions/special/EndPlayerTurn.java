package eatyourbeets.actions.special;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.TimeWarpTurnEndEffect;
import eatyourbeets.actions.EYBAction;
import eatyourbeets.utilities.GameEffects;

public class EndPlayerTurn extends EYBAction
{
    public EndPlayerTurn()
    {
        super(ActionType.SPECIAL);

        Initialize(1);
    }

    @Override
    protected void FirstUpdate()
    {
        if (!AbstractDungeon.actionManager.turnHasEnded)
        {
            CardCrawlGame.sound.play("POWER_TIME_WARP", 0.05f);
            GameEffects.Queue.Add(new BorderFlashEffect(Color.GOLD, true));
            GameEffects.TopLevelQueue.Add(new TimeWarpTurnEndEffect());

            AbstractDungeon.actionManager.callEndTurnEarlySequence();
        }

        Complete();
    }
}
