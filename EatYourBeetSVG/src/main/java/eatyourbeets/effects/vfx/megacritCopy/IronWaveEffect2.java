package eatyourbeets.effects.vfx.megacritCopy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.IronWaveParticle;
import eatyourbeets.effects.EYBEffect;
import eatyourbeets.utilities.Mathf;

public class IronWaveEffect2 extends EYBEffect
{
    private static final float BASE_STEP_X = 160f * Settings.scale;
    private static final float BASE_STEP_Y = 15f * Settings.scale;
    private static final float WAVE_DELAY = 0.03f;

    private float waveTimer = 0.0F;
    private float x;
    private float y;
    private float targetX;
    private float stepX;
    private float stepY;

    public IronWaveEffect2(float x, float y, float targetX)
    {
        this.x = x + 120.0F * Settings.scale;
        this.y = y - 20.0F * Settings.scale;
        this.targetX = targetX;
        this.stepX = targetX > x ? BASE_STEP_X : -BASE_STEP_X;
        this.stepY = BASE_STEP_Y;

        SetDuration(WAVE_DELAY * Mathf.Abs(targetX - x) / BASE_STEP_X, isRealtime);
    }

    public void update()
    {
        this.waveTimer -= Gdx.graphics.getDeltaTime();
        if (this.waveTimer < 0.0F)
        {
            this.waveTimer = WAVE_DELAY;
            this.x += stepX;
            this.y -= stepY;
            AbstractDungeon.effectsQueue.add(new IronWaveParticle(this.x, this.y));
            if (Mathf.Abs(x - targetX) < stepX)
            {
                this.isDone = true;
                CardCrawlGame.sound.playA("ATTACK_DAGGER_6", -0.3F);
            }
        }
    }

    public void render(SpriteBatch sb)
    {
    }

    public void dispose()
    {
    }
}
