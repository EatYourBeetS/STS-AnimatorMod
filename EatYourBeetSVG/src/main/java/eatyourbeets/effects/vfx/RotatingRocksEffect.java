package eatyourbeets.effects.vfx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import eatyourbeets.effects.EYBEffect;
import eatyourbeets.orbs.animator.Earth;

public class RotatingRocksEffect extends EYBEffect
{
    protected AbstractGameEffect hitEffect;
    protected float x;
    protected float y;
    protected float startX;
    protected float startY;
    protected float vR;
    protected float sizeX;
    protected float sizeY;
    protected float vfxTimer;
    protected float spread;
    protected float baseDuration;
    protected int count;

    public RotatingRocksEffect(float x, float y, int number)
    {
        super(0.25f * number);

        this.color = Color.WHITE.cpy();
        this.color.a = 0;
        this.startX = x;
        this.startY = y;
        this.x = startX;
        this.y = startY;
        this.vR = 0;
        this.sizeX = Earth.PROJECTILE_LARGE.getWidth();
        this.sizeY = Earth.PROJECTILE_LARGE.getHeight();
        this.vfxTimer = 0.23f;
        this.count = number;
        this.spread = 0f;
        this.baseDuration = 0.25f * number;
    }

    public RotatingRocksEffect SetColor(Color mainColor)
    {
        this.color = mainColor.cpy();
        this.color.a = 0;
        return this;
    }

    public RotatingRocksEffect SetImageParameters(float scale, float vR, float rotation)
    {
        this.scale = scale;
        this.vR = vR;
        this.rotation = rotation;

        return this;
    }


    @Override
    protected void UpdateInternal(float deltaTime)
    {
        color.a = Interpolation.fade.apply(0f, 1f, (this.baseDuration - this.duration) * 10);
        spread = Interpolation.exp5Out.apply(0f, 64f, (this.baseDuration - this.duration));
        rotation += vR * deltaTime;

        vfxTimer -= deltaTime;
        if (vfxTimer < 0f)
        {
            vfxTimer = 0.23f;
            count -= 1;
        }

        if (TickDuration(deltaTime))
        {
            Complete();
        }
    }

    @Override
    public void render(SpriteBatch sb)
    {
        sb.setColor(this.color);
        for (int i = count - 1; i >= 0; i--)
        {
            sb.draw(Earth.PROJECTILE_LARGE, this.x - 48 + GetCosVariance(spread, i), this.y - 48 + GetSinVariance(spread, i), this.sizeX / 2.0F, this.sizeY / 2.0F, this.sizeX, this.sizeY, this.scale, this.scale, this.rotation, 0, 0, (int) this.sizeX, (int) this.sizeY, false, false);
        }
    }

    public static float GetCosVariance(float magnitude, float time)
    {
        return magnitude * MathUtils.cosDeg(time * 45);
    }

    public static float GetSinVariance(float magnitude, float time)
    {
        return magnitude * MathUtils.sinDeg(time * 45);
    }
}
