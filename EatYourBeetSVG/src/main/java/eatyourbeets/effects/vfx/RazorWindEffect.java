package eatyourbeets.effects.vfx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.core.Settings;
import eatyourbeets.effects.EYBEffect;
import eatyourbeets.ui.TextureCache;
import eatyourbeets.utilities.GameEffects;

public class RazorWindEffect extends EYBEffect
{
    public static TextureCache image = IMAGES.AirSlice;
    public static final int SIZE = 256;

    protected float x;
    protected float y;
    protected float targetY;
    protected float horizontalAcceleration;
    protected float horizontalSpeed;
    protected float rotationSpeed;
    protected float vfxTimer;

    public RazorWindEffect(float x, float y, float targetY, float horizontalSpeed, float horizontalAcceleration)
    {
        super(1f);

        this.x = x - (SIZE / 2f);
        this.y = y - (SIZE / 2f);
        this.targetY = targetY - (SIZE /2f);
        this.rotation = Random(5f, 10f);
        this.scale = Settings.scale;
        this.horizontalSpeed = horizontalSpeed * Settings.scale;
        this.horizontalAcceleration = horizontalSpeed * Settings.scale;
        this.rotationSpeed = Random(1000f, 1200f);
        this.color = Color.WHITE.cpy();
    }

    @Override
    protected void UpdateInternal(float deltaTime)
    {
        x += horizontalSpeed * deltaTime;
        y = Interpolation.pow2OutInverse.apply(y, targetY, Math.min(1f, (1f - duration) / 2f));
        horizontalSpeed += horizontalAcceleration * deltaTime;
        rotation += rotationSpeed * deltaTime;

        if ((1f - duration) < 0.1f)
        {
            color.a = Interpolation.fade.apply(0.1f, 1f, (1f - duration) * 7f);
        }
        else
        {
            color.a = Interpolation.pow2Out.apply(0.1f, 1f, duration);
        }

        vfxTimer -= deltaTime;
        if (vfxTimer < 0f)
        {
            vfxTimer = 0.007f;
            GameEffects.Queue.Add(new RazorWindParticleEffect(x, y + (SIZE / 2f) + Random(-100,100), Math.signum(horizontalSpeed) * Random(-300f, -50f) * Settings.scale, Random(-200f, 200f) * Settings.scale));
        }

        super.UpdateInternal(deltaTime);
    }

    public void render(SpriteBatch sb)
    {
        RenderImage(sb, image.Texture(), x, y, false, false);
    }
}
