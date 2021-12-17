package pinacolada.actions.player;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.TimeWarpTurnEndEffect;
import eatyourbeets.actions.EYBAction;
import pinacolada.utilities.PCLGameEffects;
import pinacolada.utilities.PCLGameUtilities;

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
        if (PCLGameUtilities.IsPlayerTurn())
        {
            CardCrawlGame.sound.play("POWER_TIME_WARP", 0.05f);
            PCLGameEffects.Queue.Add(new BorderFlashEffect(Color.GOLD, true));
            PCLGameEffects.TopLevelQueue.Add(new TimeWarpTurnEndEffect());

            AbstractDungeon.actionManager.callEndTurnEarlySequence();
        }

        Complete();
    }
}
