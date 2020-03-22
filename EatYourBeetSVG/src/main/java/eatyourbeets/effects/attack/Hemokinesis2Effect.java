package eatyourbeets.effects.attack;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import eatyourbeets.utilities.GameEffects;

public class Hemokinesis2Effect extends AbstractGameEffect
{
    private final float x;
    private final float y;
    private final float tX;
    private final float tY;

    public Hemokinesis2Effect(float x, float y, float targetX, float targetY)
    {
        this.x = x;
        this.y = y;
        this.tX = targetX;
        this.tY = targetY;
        this.scale = 0.12f;
        this.duration = 0.5f;
    }

    public void update()
    {
        this.scale -= Gdx.graphics.getDeltaTime();
        if (this.scale < 0f)
        {
            GameEffects.Queue.Add(new HemokinesisParticle2Effect(this.x + MathUtils.random(60f, -60f) * Settings.scale, this.y + MathUtils.random(60f, -60f) * Settings.scale, this.tX, this.tY, this.x > this.tX));
            this.scale = 0.04f;
        }

        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration < 0f)
        {
            this.isDone = true;
        }

    }

    public void render(SpriteBatch sb)
    {
    }

    public void dispose()
    {
    }
}
