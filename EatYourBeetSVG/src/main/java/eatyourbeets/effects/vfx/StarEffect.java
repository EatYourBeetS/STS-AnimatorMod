package eatyourbeets.effects.vfx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import eatyourbeets.effects.EYBEffect;
import eatyourbeets.utilities.AdvancedTexture;
import eatyourbeets.utilities.GameEffects;

import java.util.ArrayList;

public class StarEffect extends EYBEffect
{
    protected static final ArrayList<Float> RGB = new ArrayList<>(3);
    protected static Texture img;

    protected float x;
    protected float y;
    protected float horizontalSpeed;
    protected float verticalSpeed;
    protected float rotationSpeed;
    protected float vfxTimer;
    protected AdvancedTexture projectile;

    public StarEffect(float x, float y, float horizontalSpeedMin, float horizontalSpeedMax, float verticalSpeedMin, float verticalSpeedMax)
    {
        super(Random(0.5f, 1f));

        if (img == null)
        {
            img = ImageMaster.loadImage("images/effects/Star.png");
        }

        SetRandomColor();

        this.projectile = new AdvancedTexture(img, 48f, 48f)
                .SetPosition(x, y)
                .SetSpeed(Random(horizontalSpeedMin, horizontalSpeedMax) * Settings.scale, Random(verticalSpeedMin, verticalSpeedMax) * Settings.scale, Random(-600f, 600f))
        .SetScale(Random(0.2f, 3.0f) * Settings.scale)
                .SetTargetScale(0.3f)
        .SetRotation(Random(5f, 10f)).SetFlip(RandomBoolean(0.5f), null)
        .SetColor(this.color);
    }

    public StarEffect SetRandomColor()
    {
        RGB.add(0f);
        RGB.add(1f);
        RGB.add(Random(0.5f, 1f));

        this.color = new Color(RGB.remove(Random(0, 2)), RGB.remove(Random(0, 1)), RGB.remove(0), 0.15f);

        return this;
    }

    @Override
    protected void UpdateInternal(float deltaTime)
    {

        if ((1f - duration) < 0.1f)
        {
            color.a = Interpolation.fade.apply(0.1f, 1f, (1f - duration) * 10f);
        }
        else
        {
            color.a = Interpolation.pow2Out.apply(0.1f, 1f, duration);
        }
        this.projectile.color.a = this.color.a;
        this.projectile.Update(deltaTime);

        vfxTimer -= deltaTime;
        if (vfxTimer < 0f)
        {
            vfxTimer = 0.016f;
            GameEffects.Queue.Add(new StarParticleEffect(x, y, Color.WHITE));
        }

        super.UpdateInternal(deltaTime);
    }

    public void render(SpriteBatch sb)
    {
        this.projectile.Render(sb);
    }
}
