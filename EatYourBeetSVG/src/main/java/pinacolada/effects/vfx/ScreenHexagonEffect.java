package pinacolada.effects.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.BorderLongFlashEffect;
import pinacolada.effects.PCLEffect;
import pinacolada.effects.SFX;
import pinacolada.utilities.PCLGameEffects;

public class ScreenHexagonEffect extends PCLEffect
{
    private float timer = 0.0F;
    private static final float INTERVAL = 0.05F;
    private static final int ROWS = 6;

    public ScreenHexagonEffect() {
        this.color = Color.GOLDENROD.cpy();
        this.duration = 1.5F;
        this.startingDuration = this.duration;
    }

    public void update() {
        if (this.duration == this.startingDuration) {
            SFX.Play(SFX.PCL_BOOST, 0.5f, 0.5f);
            SFX.Play(SFX.ORB_LIGHTNING_CHANNEL, 0.7f, 0.7f);
            AbstractDungeon.effectsQueue.add(new BorderLongFlashEffect(Color.GOLDENROD));
        }

        this.duration -= Gdx.graphics.getDeltaTime();
        this.timer -= Gdx.graphics.getDeltaTime();
        if (this.timer < 0.0F) {
            MakeHexagons();
            this.timer = 0.05F;
        }

        if (this.duration < 0.0F) {
            this.isDone = true;
        }

    }

    public void MakeHexagons() {
        for (int i = 0; i < ROWS; i++) {
            PCLGameEffects.Queue.Add(new HexagonEffect(-100f * Settings.scale,  Settings.HEIGHT * 0.65f - i * HexagonEffect.SIZE * 0.9f, color)
                    .SetRealtime(isRealtime));
        }
    }

    public void render(SpriteBatch sb) {
    }

    public void dispose() {
    }
}
