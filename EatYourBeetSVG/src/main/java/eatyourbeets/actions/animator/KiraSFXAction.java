package eatyourbeets.actions.animator;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.vfx.BorderLongFlashEffect;
import eatyourbeets.utilities.GameActionsHelper;

public class KiraSFXAction extends AnimatorAction
{
    private final String key;
    private float pitchVar = 0.0F;

    public KiraSFXAction(String key)
    {
        this.key = key;
        this.actionType = ActionType.WAIT;
    }

    public KiraSFXAction(String key, float pitchVar)
    {
        this.key = key;
        this.pitchVar = pitchVar;
        this.actionType = ActionType.WAIT;
    }

    public void update()
    {
        CardCrawlGame.music.playTempBGM(this.key);//.play(this.key, this.pitchVar);
        GameActionsHelper.AddToBottom(new VFXAction(new BorderLongFlashEffect(Color.VIOLET)));
//        AbstractDungeon.effectsQueue.add(new CallbackEffect(new WaitRealtimeAction(12), this::OnCompletion, this));
        this.isDone = true;
    }

    private void OnCompletion(Object state, AbstractGameAction action)
    {
        if (state != null && action != null)
        {
            CardCrawlGame.music.unsilenceBGM();
        }
    }
}