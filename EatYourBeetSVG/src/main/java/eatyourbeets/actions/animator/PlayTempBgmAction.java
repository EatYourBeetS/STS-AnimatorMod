package eatyourbeets.actions.animator;

import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import eatyourbeets.actions.common.WaitRealtimeAction;

public class PlayTempBgmAction extends AnimatorAction
{
    private WaitRealtimeAction wait;
    private final String key;
    private float delay;

    public PlayTempBgmAction(String key)
    {
        this.key = key;
        this.delay = 0.0F;
        this.actionType = ActionType.WAIT;
        this.wait = new WaitRealtimeAction(0);
    }

    public PlayTempBgmAction(String key, float delay)
    {
        this.key = key;
        this.delay = delay;
        this.actionType = ActionType.WAIT;
        this.wait = new WaitRealtimeAction(delay);
    }

    public void update()
    {
        wait.update();
        if (wait.isDone)
        {
            CardCrawlGame.music.playTempBgmInstantly(this.key);

            this.isDone = true;
        }
    }
}