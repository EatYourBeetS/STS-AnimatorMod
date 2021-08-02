package eatyourbeets.effects.vfx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import eatyourbeets.effects.EYBEffect;
import eatyourbeets.ui.TextureCache;

public class RazorWindParticleEffect extends EYBEffect
{
    protected static final int SIZE = 96;
    protected static TextureCache image = IMAGES.AirTrail;

    protected float x;
    protected float y;
    protected float horizontalSpeed;
    protected float verticalSpeed;
    protected float rotationSpeed;
    protected float alpha;


    public RazorWindParticleEffect(float x, float y, float horizontalSpeed, float verticalSpeed)
    {
        super(MathUtils.random(0.4F, 0.8F));

        final float offsetX = MathUtils.random(-16.0F, 16.0F) * Settings.scale;
        final float offsetY = MathUtils.random(-16.0F, 16.0F) * Settings.scale;
        if (offsetX > 0.0F)
        {
            this.renderBehind = true;
        }

        this.x = x + offsetX - (float) (SIZE / 2);
        this.y = y + offsetY - (float) (SIZE / 2);
        this.horizontalSpeed = horizontalSpeed * Settings.scale;
        this.verticalSpeed = verticalSpeed * Settings.scale;
        this.scale = Random(0.04f, 0.6f) * Settings.scale;
        this.alpha = Random(0.3F, 1.0F);
        this.color = Color.WHITE.cpy();
        this.color.a = this.alpha;
        this.rotation = Random(-10f, 10f);
        this.rotationSpeed = Random(500f, 800f);
        if (RandomBoolean(0.5f)) this.rotationSpeed *= -1;
    }

    @Override
    protected void UpdateInternal(float deltaTime)
    {
        super.UpdateInternal(deltaTime);
        x += horizontalSpeed * deltaTime;
        y += verticalSpeed * deltaTime;
        rotation += rotationSpeed * deltaTime;

        final float halfDuration = startingDuration * 0.5f;
        if (this.duration < halfDuration)
        {
            this.color.a = Interpolation.exp5In.apply(0.0F, this.alpha, this.duration / halfDuration);
        }
    }

    public void render(SpriteBatch sb)
    {
        sb.setColor(this.color);
        sb.setBlendFunction(770, 1);
        sb.draw(image.Texture(), x, y, SIZE * 0.5f, SIZE * 0.5f, SIZE, SIZE, scale, scale, rotation, 0, 0, SIZE, SIZE, RandomBoolean(0.5f), false);
        sb.setBlendFunction(770, 771);
    }
}
