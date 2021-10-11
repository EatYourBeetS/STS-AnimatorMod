package eatyourbeets.effects.vfx.megacritCopy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.BorderLongFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.GiantFireEffect;
import eatyourbeets.effects.EYBEffect;

public class ScreenOnFireEffect2 extends EYBEffect
{
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
            AbstractDungeon.effectsQueue.add(new GiantFireEffect());
            AbstractDungeon.effectsQueue.add(new GiantFireEffect());
            AbstractDungeon.effectsQueue.add(new GiantFireEffect());
            AbstractDungeon.effectsQueue.add(new GiantFireEffect());
            AbstractDungeon.effectsQueue.add(new GiantFireEffect());
            AbstractDungeon.effectsQueue.add(new GiantFireEffect());
            AbstractDungeon.effectsQueue.add(new GiantFireEffect());
            AbstractDungeon.effectsQueue.add(new GiantFireEffect());
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
