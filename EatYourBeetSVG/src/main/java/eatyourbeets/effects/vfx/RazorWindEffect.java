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

public class RazorWindEffect extends EYBEffect
{
    public static Texture img;
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

        if (img == null) {
            img = ImageMaster.loadImage("images/orbs/animator/AirSlice.png");
        }

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
            GameEffects.Queue.Add(new RazorWindParticleEffect(x, y + (SIZE / 2) + Random(-100,100), Math.signum(horizontalSpeed) * Random(-300f, -50f) * Settings.scale, Random(-200f, 200f) * Settings.scale));
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
