package eatyourbeets.effects.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.BorderLongFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.LightFlareParticleEffect;
import eatyourbeets.effects.EYBEffect;
import eatyourbeets.effects.SFX;
import eatyourbeets.utilities.GameEffects;

public class ScreenFreezingEffect extends EYBEffect
{
    private float timer = 0.0F;
    private static final float INTERVAL = 0.05F;
    private static final int TIMES = 9;

    public ScreenFreezingEffect() {
        this.color = Color.SKY.cpy();
        this.duration = 3.0F;
        this.startingDuration = this.duration;
    }

    public void update() {
        if (this.duration == this.startingDuration) {
            SFX.Play(SFX.HEAL_3);
            AbstractDungeon.effectsQueue.add(new BorderLongFlashEffect(Color.NAVY));
        }

        this.duration -= Gdx.graphics.getDeltaTime();
        this.timer -= Gdx.graphics.getDeltaTime();
        if (this.timer < 0.0F) {
            for (int i = 0; i < TIMES; i++) {
                MakeSnowflake();
            }
            this.timer = 0.05F;
        }

        if (this.duration < 0.0F) {
            this.isDone = true;
        }

    }

    public void MakeSnowflake() {
        GameEffects.Queue.Add(new SnowballParticleEffect(MathUtils.random(0.0F, (float) Settings.WIDTH),  MathUtils.random(900.0F, 1100.0F) * Settings.scale, color)
                .SetSpeed(MathUtils.random(-70.0F, 70.0F) * Settings.scale, MathUtils.random(-1100.0F, -450.0F) * Settings.scale)
                .SetRealtime(isRealtime));
        if (MathUtils.randomBoolean()) {
            GameEffects.Queue.Add(new LightFlareParticleEffect(MathUtils.random(0.0F, (float) Settings.WIDTH), MathUtils.random(100.0F, 700.0F) * Settings.scale, color));
        }
    }

    public void render(SpriteBatch sb) {
    }

    public void dispose() {
    }
}
