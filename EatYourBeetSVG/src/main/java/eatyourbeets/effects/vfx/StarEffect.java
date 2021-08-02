package eatyourbeets.effects.vfx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import eatyourbeets.effects.EYBEffect;
import eatyourbeets.ui.TextureCache;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.Mathf;

import java.util.ArrayList;

public class StarEffect extends EYBEffect
{
    protected static final ArrayList<Float> RGB = new ArrayList<>(3);
    protected static final TextureCache image = IMAGES.Star;

    protected float vfxFrequency = 0.016f;

    protected float horizontalSpeed;
    protected float verticalSpeed;
    protected float rotationSpeed;
    protected float x;
    protected float y;
    protected float vfxTimer;

    public StarEffect(float x, float y, float horizontalSpeed, float verticalSpeed)
    {
        super(Random(0.5f, 1f));

        this.x = x;
        this.y = y;
        this.rotation = Random(5f, 10f);
        this.scale = Random(0.2f, 3.0f);
        this.horizontalSpeed = horizontalSpeed;
        this.verticalSpeed = verticalSpeed;
        this.rotationSpeed = Random(-600f, 600f);

        if (RandomBoolean())
        {
            this.rotation *= -1;
        }

        SetRandomColor();
    }

    public StarEffect SetRandomColor()
    {
        RGB.clear();
        RGB.add(0f);
        RGB.add(1f);
        RGB.add(Random(0.5f, 1f));

        this.color = new Color(RGB.remove(Random(0, 2)), RGB.remove(Random(0, 1)), RGB.remove(0), 0.15f);

        return this;
    }

    public StarEffect SetFrequency(float frequency)
    {
        this.vfxFrequency = Mathf.Clamp(frequency, 0.01f, startingDuration / 5f);

        return this;
    }

    @Override
    protected void UpdateInternal(float deltaTime)
    {
        x += horizontalSpeed * deltaTime;
        y += verticalSpeed * deltaTime;
        rotation += rotationSpeed * deltaTime;

        if (scale > 0.3f)
        {
            scale -= deltaTime * 2f;
        }

        if ((1f - duration) < 0.1f)
        {
            color.a = Interpolation.fade.apply(0.1f, 1f, (1f - duration) * 10f);
        }
        else
        {
            color.a = Interpolation.pow2Out.apply(0.1f, 1f, duration);
        }

        vfxTimer -= deltaTime;
        if (vfxTimer < 0f)
        {
            GameEffects.Queue.Add(new StarParticleEffect(x, y, Color.WHITE));
            vfxTimer = vfxFrequency;
        }

        super.UpdateInternal(deltaTime);
    }

    public void render(SpriteBatch sb)
    {
        RenderImage(sb, image.Texture(), x, y, false, false);
    }
}
