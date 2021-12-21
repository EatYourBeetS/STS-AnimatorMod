package pinacolada.effects.vfx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import eatyourbeets.effects.EYBEffect;
import eatyourbeets.utilities.Colors;
import eatyourbeets.utilities.Mathf;
import pinacolada.effects.VFX;
import pinacolada.ui.TextureCache;
import pinacolada.utilities.PCLGameEffects;

import java.util.ArrayList;

public class StarEffect extends EYBEffect
{
    protected static final ArrayList<Float> RGB = new ArrayList<>(3);
    protected static final TextureCache image = VFX.IMAGES.Star;

    protected float vfxFrequency = 0.015f;
    protected float horizontalSpeed;
    protected float verticalSpeed;
    protected float rotationSpeed;
    protected float x;
    protected float y;
    protected float vfxTimer;

    public StarEffect(float x, float y, float horizontalSpeed, float verticalSpeed) {
        this(x,y,horizontalSpeed,verticalSpeed,Random(-600f, 600f),Random(0.5f, 3.0f));
    }

    public StarEffect(float x, float y, float horizontalSpeed, float verticalSpeed, float rotationSpeed, float scale)
    {
        super(Random(0.5f, 1f));

        this.x = x;
        this.y = y;
        this.scale = scale;
        this.rotation = Random(-10f, 10f);
        this.horizontalSpeed = horizontalSpeed;
        this.verticalSpeed = verticalSpeed;
        this.rotationSpeed = rotationSpeed;

        if (RandomBoolean())
        {
            this.rotation *= -1;
        }

        SetRandomColor();
    }

    public StarEffect SetRandomColor()
    {
        RGB.clear();
        RGB.add(0.48f);
        RGB.add(1f);
        RGB.add(Random(0.48f, 1f));

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
            PCLGameEffects.Queue.Add(new StarParticleEffect(x, y, Random(-120f, -30f) * Math.signum(horizontalSpeed), Random(-60f, 60f), Random(0.01f, 0.28f) * Math.min(1f,this.scale), Colors.Random(0.83f,1f,false)));
            if (RandomBoolean(0.72f) && this.scale >= 0.7f) {
                PCLGameEffects.Queue.Add(new StarEffect(x, y, horizontalSpeed * -0.25f, Random(-horizontalSpeed * 0.05f, horizontalSpeed * 0.05f), Random(-1000f, 1000f), Random(0.05f,Math.min(0.5f,this.scale))));
            }
            vfxTimer = vfxFrequency;
        }

        super.UpdateInternal(deltaTime);
    }

    public void render(SpriteBatch sb)
    {
        RenderImage(sb, image.Texture(), x, y, false, false);
    }
}
