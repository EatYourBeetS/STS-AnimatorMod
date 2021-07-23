package eatyourbeets.effects.vfx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import eatyourbeets.effects.EYBEffect;
import eatyourbeets.effects.Projectile;
import eatyourbeets.utilities.GameEffects;

import java.util.ArrayList;

public class RazorWindEffect extends EYBEffect
{
    protected static final ArrayList<Float> RGB = new ArrayList<>(3);
    protected static Texture img;

    protected float x;
    protected float y;
    protected float horizontalAcceleration;
    protected float horizontalSpeed;
    protected float rotationSpeed;
    protected float vfxTimer;
    protected Projectile projectile;

    public RazorWindEffect(float x, float y, float horizontalSpeed, float horizontalAcceleration)
    {
        super(1f);

        if (img == null) {
            img = ImageMaster.loadImage("images/orbs/animator/AirSlice.png");
        }

        this.projectile = new Projectile(img, 48f, 48f)
        .SetPosition(x,y).SetTargetPosition((float)Settings.WIDTH, y).SetTargetRotation(96000).SetScale(Settings.scale * 2).SetSpeed(horizontalSpeed  * Settings.scale, 0f, 600f).SetSpeedMulti(horizontalAcceleration  * Settings.scale, 0f, 0f);
        this.color = Color.WHITE.cpy();
    }


    @Override
    protected void UpdateInternal(float deltaTime)
    {
        this.projectile.Update(deltaTime);

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
            GameEffects.Queue.Add(new RazorWindParticleEffect(x, y));
        }

        super.UpdateInternal(deltaTime);
    }

    public void render(SpriteBatch sb)
    {
        this.projectile.Render(sb, this.color);
    }
}
