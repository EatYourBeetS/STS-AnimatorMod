package eatyourbeets.actions.animator;

import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.core.CardCrawlGame;

public class PlayTempBgmAction extends AnimatorAction
{
    private final String key;
    private float delay;

    public PlayTempBgmAction(String key)
    {
        this.key = key;
        this.delay = 0.0F;
        this.actionType = ActionType.WAIT;
    }

    public PlayTempBgmAction(String key, float delay)
    {
        this.key = key;
        this.delay = delay;
        this.actionType = ActionType.WAIT;
    }

    public void update()
    {
        this.delay -= Gdx.graphics.getDeltaTime();
        if (delay <= 0)
        {
            CardCrawlGame.music.playTempBGM(this.key);

            this.isDone = true;
        }
    }
}