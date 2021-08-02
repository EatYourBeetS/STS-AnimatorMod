package eatyourbeets.effects.vfx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import eatyourbeets.effects.EYBEffect;
import eatyourbeets.effects.Projectile;

public class FadingParticleEffect extends EYBEffect
{
    protected Projectile projectile;
    protected float x;
    protected float y;
    protected float alpha;
    protected boolean isTranslucent;

    public FadingParticleEffect(Texture texture, float x, float y, float size)
    {
        this.x = x;
        this.y = y;
        this.alpha = 1.0F;

        this.projectile = new Projectile(texture, size, size);
        this.projectile.SetPosition(x, y).SetTargetPosition(x, y);
        this.isTranslucent = false;
    }

    public FadingParticleEffect SetColor(Color color)
    {
        this.projectile.SetColor(color);
        return this;
    }

    public FadingParticleEffect SetScale(float scale)
    {
        this.projectile.scale = scale;
        return this;
    }

    public FadingParticleEffect SetSpeed(float vX, float vY, float vR)
    {
        this.projectile.SetSpeed(vX, vY, vR);

        return this;
    }

    public FadingParticleEffect SetRotation(float rotation)
    {
        this.rotation = rotation;
        return this;
    }

    public FadingParticleEffect SetTargetPosition(float x, float y)
    {
        this.projectile.SetTargetPosition(x, y);
        return this;
    }

    public FadingParticleEffect SetTargetRotation(float degrees)
    {
        this.projectile.SetTargetRotation(degrees);

        return this;
    }

    public FadingParticleEffect SetTranslucent(float alpha)
    {
        this.isTranslucent = true;
        this.alpha = alpha;
        this.projectile.color.a = this.alpha;
        return this;
    }

    @Override
    protected void UpdateInternal(float deltaTime)
    {
        super.UpdateInternal(deltaTime);
        this.projectile.Update(deltaTime);

        final float halfDuration = startingDuration * 0.5f;
        if (this.duration < halfDuration)
        {
            this.projectile.color.a = Interpolation.exp5In.apply(0.0F, this.alpha, this.duration / halfDuration);
        }
    }

    @Override
    public void render(SpriteBatch sb)
    {
        if (projectile != null)
        {
            if (isTranslucent)
            {
                sb.setBlendFunction(770, 1);
                projectile.Render(sb);
                sb.setBlendFunction(770, 771);
            }
            else
            {
                projectile.Render(sb);
            }
        }
    }

    public void dispose()
    {
    }
}
