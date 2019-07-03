package eatyourbeets.actions.animator;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.actions.common.WaitRealtimeAction;
import patches.MusicMasterPatch;

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
            if (!MusicMasterPatch.PlayTempBgmInstantly.AlreadyPlaying(CardCrawlGame.music, this.key))
            {
                CardCrawlGame.music.silenceBGM();
                AbstractDungeon.scene.fadeOutAmbiance();
                CardCrawlGame.music.playTempBgmInstantly(this.key, true);
                CardCrawlGame.music.updateVolume();
            }

            this.isDone = true;
        }
    }
}