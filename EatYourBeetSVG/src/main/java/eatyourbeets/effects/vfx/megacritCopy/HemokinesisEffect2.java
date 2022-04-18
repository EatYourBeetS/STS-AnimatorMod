package eatyourbeets.effects.vfx.megacritCopy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.core.Settings;
import eatyourbeets.effects.EYBEffect;
import eatyourbeets.effects.vfx.HemokinesisParticleEffect;
import eatyourbeets.utilities.GameEffects;

public class HemokinesisEffect2 extends EYBEffect
{
    private final float x;
    private final float y;
    private final float tX;
    private final float tY;
    private float timer;
    private float vfxFrequency;

    public HemokinesisEffect2(float x, float y, float targetX, float targetY)
    {
        super(0.5f);

        this.color = new Color(1f, 0f, 0.02f, 0.6f);
        this.x = x;
        this.y = y;
        this.tX = targetX;
        this.tY = targetY;
        this.scale = 1;
        this.timer = 0.12f;
        this.vfxFrequency = 0.04f;
    }

    public HemokinesisEffect2 SetFrequency(float frequency)
    {
        this.vfxFrequency = frequency;

        return this;
    }

    public HemokinesisEffect2 SetScale(float scale)
    {
        this.scale = scale;

        return this;
    }

    public HemokinesisEffect2 SetColor(Color color)
    {
        this.color = color.cpy();
        this.color.a = 0.6f;

        return this;
    }

    public void update()
    {
        this.timer -= Gdx.graphics.getDeltaTime();
        if (this.timer < 0f)
        {
            GameEffects.Queue.Add(new HemokinesisParticleEffect(this.x + Random(60f, -60f) * Settings.scale, this.y + Random(60f, -60f) * Settings.scale, this.tX, this.tY, this.x > this.tX, color))
            .SetScale(scale);
            this.timer = vfxFrequency;
        }

        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration < 0f)
        {
            this.isDone = true;
        }
    }
}
