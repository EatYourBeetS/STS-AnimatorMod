package eatyourbeets.effects.vfx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import eatyourbeets.effects.EYBEffect;
import eatyourbeets.utilities.GameEffects;

import java.util.ArrayList;

public class StarEffect extends EYBEffect
{
    protected static final ArrayList<Float> RGB = new ArrayList<>(3);
    protected static final int SIZE = 96;

    protected Texture img;
    protected float x;
    protected float y;
    protected float horizontalSpeed;
    protected float verticalSpeed;
    protected float rotationSpeed;
    protected float vfxTimer;

    public StarEffect(float x, float y, float horizontalSpeedMin, float horizontalSpeedMax, float verticalSpeedMin, float verticalSpeedMax)
    {
        super(Random(0.5f, 1f));

        this.img = ImageMaster.loadImage("images/effects/Star.png");
        this.x = x - (SIZE / 2f);
        this.y = y - (SIZE / 2f);
        this.rotation = Random(5f, 10f);
        this.scale = Random(0.2f, 3.0f) * Settings.scale;
        this.horizontalSpeed = Random(horizontalSpeedMin, horizontalSpeedMax) * Settings.scale;
        this.verticalSpeed = Random(verticalSpeedMin, verticalSpeedMax) * Settings.scale;
        this.rotationSpeed = Random(-600f, 600f);

        if (RandomBoolean(0.5f))
        {
            this.rotation *= -1;
        }

        SetRandomColor();
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
        x += horizontalSpeed * deltaTime;
        y += verticalSpeed * deltaTime;
        rotation += rotationSpeed * deltaTime;
        if (scale > 0.3f * Settings.scale)
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
            vfxTimer = 0.016f;
            GameEffects.Queue.Add(new StarParticleEffect(x, y, Color.WHITE));
        }

        super.UpdateInternal(deltaTime);
    }

    public void render(SpriteBatch sb)
    {
        sb.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
        sb.setColor(this.color);
        sb.draw(img, x, y, SIZE * 0.5f, SIZE * 0.5f, SIZE, SIZE, scale, scale, rotation, 0, 0, SIZE, SIZE, false, false);
        sb.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
    }
}
