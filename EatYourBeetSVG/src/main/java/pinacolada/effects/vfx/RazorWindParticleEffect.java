package pinacolada.effects.vfx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import eatyourbeets.effects.EYBEffect;
import pinacolada.effects.VFX;
import pinacolada.ui.TextureCache;
import pinacolada.utilities.PCLJUtils;

public class RazorWindParticleEffect extends EYBEffect
{
    protected static final int SIZE = 96;
    protected static final TextureCache[] images = {VFX.IMAGES.AirTrail1,VFX.IMAGES.AirTrail2,VFX.IMAGES.AirTrail3};

    protected float x;
    protected float y;
    protected float horizontalSpeed;
    protected float verticalSpeed;
    protected float rotationSpeed;
    protected float alpha;
    protected Texture image;

    public RazorWindParticleEffect(float x, float y, float horizontalSpeed, float verticalSpeed)
    {
        super(MathUtils.random(0.4F, 0.8F));

        final float offsetX = MathUtils.random(-16.0F, 16.0F) * Settings.scale;
        final float offsetY = MathUtils.random(-16.0F, 16.0F) * Settings.scale;
        if (offsetX > 0.0F)
        {
            this.renderBehind = true;
        }

        this.x = x + offsetX;
        this.y = y + offsetY;
        this.horizontalSpeed = horizontalSpeed * Settings.scale;
        this.verticalSpeed = verticalSpeed * Settings.scale;
        this.scale = Random(0.04f, 0.6f);
        this.alpha = Random(0.3F, 1.0F);
        this.color = new Color(MathUtils.random(0.8f, 1f), 1f, MathUtils.random(0.8f, 1f), 1);
        this.color.a = this.alpha;
        this.rotation = Random(-10f, 10f);
        this.rotationSpeed = Random(500f, 800f);
        this.image = PCLJUtils.Random(images).Texture();

        if (RandomBoolean())
        {
            this.rotationSpeed *= -1;
        }
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
        RenderImage(sb, image, x, y, false, false);
    }
}
