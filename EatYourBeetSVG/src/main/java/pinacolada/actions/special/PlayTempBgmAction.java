package pinacolada.actions.special;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.actions.EYBAction;
import eatyourbeets.actions.utility.WaitRealtimeAction;
import patches.MusicMasterPatches;

public class PlayTempBgmAction extends EYBAction
{
    private final WaitRealtimeAction wait;
    private final String key;

    public PlayTempBgmAction(String key)
    {
        this(key, 0);
    }

    public PlayTempBgmAction(String key, float delay)
    {
        super(ActionType.WAIT);

        this.key = key;
        this.wait = new WaitRealtimeAction(delay);
    }

    @Override
    public void update()
    {
        wait.update();
        if (wait.isDone)
        {
            if (!MusicMasterPatches.IsAlreadyPlaying(CardCrawlGame.music, this.key))
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