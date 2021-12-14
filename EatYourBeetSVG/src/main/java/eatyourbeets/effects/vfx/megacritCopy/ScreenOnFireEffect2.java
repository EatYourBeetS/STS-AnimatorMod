package eatyourbeets.effects.vfx.megacritCopy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.BorderLongFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.GiantFireEffect;
import eatyourbeets.effects.EYBEffect;
import eatyourbeets.utilities.FieldInfo;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.JUtils;

public class ScreenOnFireEffect2 extends EYBEffect
{
    protected static final FieldInfo<Color> _color = JUtils.GetField("color", GiantFireEffect.class);
    protected int times = 8;
    private float timer = 0.0F;
    private static final float INTERVAL = 0.05F;

    public ScreenOnFireEffect2() {
        this.duration = 3.0F;
        this.startingDuration = this.duration;
    }

    public void update() {
        if (this.duration == this.startingDuration) {
            CardCrawlGame.sound.play("GHOST_FLAMES");
            AbstractDungeon.effectsQueue.add(new BorderLongFlashEffect(Color.FIREBRICK));
        }

        this.duration -= Gdx.graphics.getDeltaTime();
        this.timer -= Gdx.graphics.getDeltaTime();
        if (this.timer < 0.0F) {
            for (int i = 0; i < times; i++) {
                GiantFireEffect f = new GiantFireEffect();
                _color.Set(f,new Color(1.0f, MathUtils.random(0.3f, 1f), MathUtils.random(0.3f, 1f), 0f));
                GameEffects.Queue.Add(new GiantFireEffect());
            }
            this.timer = 0.05F;
        }

        if (this.duration < 0.0F) {
            this.isDone = true;
        }

    }

    public void render(SpriteBatch sb) {
    }

    public void dispose() {
    }
}
