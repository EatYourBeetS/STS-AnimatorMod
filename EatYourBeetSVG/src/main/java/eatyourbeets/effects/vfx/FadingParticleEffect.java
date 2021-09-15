package eatyourbeets.effects.vfx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.core.Settings;
import eatyourbeets.effects.EYBEffect;
import eatyourbeets.effects.Projectile;
import eatyourbeets.interfaces.delegates.ActionT1;
import eatyourbeets.interfaces.delegates.ActionT2;

public class FadingParticleEffect extends EYBEffect
{
    protected Projectile projectile;
    protected float x;
    protected float y;
    protected float alpha;
    protected boolean isTranslucent;

    public FadingParticleEffect(Texture texture, float x, float y)
    {
        super(Settings.ACTION_DUR_FAST, false);

        this.projectile = new Projectile(texture, texture.getWidth(), texture.getHeight());
        this.projectile.SetPosition(x, y).SetTargetPosition(x, y);
        this.x = x;
        this.y = y;
        this.alpha = 1.0F;
        this.isTranslucent = false;
    }

    public FadingParticleEffect SetColor(Color color)
    {
        this.projectile.SetColor(color);

        return this;
    }

    public FadingParticleEffect SetRotation(float startRotation)
    {
        this.projectile.SetRotation(startRotation);

        return this;
    }

    public FadingParticleEffect SetScale(float scale)
    {
        this.projectile.pos.scale = scale;

        return this;
    }

    public FadingParticleEffect SetTargetRotation(float targetRotation, float rotationSpeed)
    {
        this.projectile.SetTargetRotation(targetRotation, rotationSpeed);

        return this;
    }


    public FadingParticleEffect SetTargetScale(float scale, float growthRate)
    {
        this.projectile.SetTargetScale(scale, growthRate);

        return this;
    }

    public FadingParticleEffect Edit(ActionT1<Projectile> action)
    {
        action.Invoke(projectile);

        return this;
    }

    public <T> FadingParticleEffect Edit(T state, ActionT2<T, Projectile> action)
    {
        action.Invoke(state, projectile);

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

    @Override
    public void dispose()
    {

    }
}
