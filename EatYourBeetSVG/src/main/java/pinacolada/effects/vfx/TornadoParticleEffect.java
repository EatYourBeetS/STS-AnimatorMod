package pinacolada.effects.vfx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.combat.LightFlareParticleEffect;
import eatyourbeets.effects.EYBEffect;
import pinacolada.effects.VFX;
import pinacolada.ui.TextureCache;
import pinacolada.utilities.PCLGameEffects;
import pinacolada.utilities.PCLJUtils;

public class TornadoParticleEffect extends EYBEffect
{
    protected static final int SIZE = 230;
    protected static final TextureCache[] images = {VFX.IMAGES.AirTornado1,VFX.IMAGES.AirTornado2};
    protected static final Color PARTICLE_COLOR = Color.LIME.cpy();

    protected float x;
    protected float y;
    protected float tX;
    protected float tY;
    protected float radius;
    protected float radiusSpeed;
    protected float rotationSpeed;
    protected float alpha;
    protected float vfxTimer = 0.3f;
    protected Color secondaryColor;
    protected Texture image;

    public TornadoParticleEffect(float x, float y, float radiusSpeed)
    {
        super(MathUtils.random(0.4F, 0.8F));

        final float offsetX = MathUtils.random(-16.0F, 16.0F) * Settings.scale;
        final float offsetY = MathUtils.random(-16.0F, 16.0F) * Settings.scale;
        if (offsetX > 0.0F)
        {
            this.renderBehind = true;
        }

        this.x = this.tX = x + offsetX;
        this.y = this.tY = y + offsetY;
        this.radius = Random(0f,10f);
        this.radiusSpeed = radiusSpeed * Settings.scale;
        this.scale = Random(0.04f, 0.75f);
        this.alpha = Random(0.3F, 1.0F);
        this.color = new Color(MathUtils.random(0.9f, 1f), 1f, MathUtils.random(0.9f, 1f), 1);
        this.color.a = this.alpha;
        this.rotation = Random(-200f, 200f);
        this.rotationSpeed = Random(500f, 800f);
        this.duration = 0.75f;
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

        vfxTimer -= deltaTime;
        radius += radiusSpeed * deltaTime;
        rotation += rotationSpeed * deltaTime;
        tX = x + radius * MathUtils.cos(rotation);
        tY = y + radius * MathUtils.sin(rotation);

        final float halfDuration = startingDuration * 0.5f;
        if (this.duration < halfDuration)
        {
            this.color.a = Interpolation.exp5In.apply(0.0F, this.alpha, this.duration / halfDuration);
        }

        if (vfxTimer < 0f)
        {
            PCLGameEffects.Queue.Add(new RazorWindParticleEffect(tX, tY,
                    Random(-300f, 300f), Random(-300f, 300f)));
            PCLGameEffects.Queue.Add(new LightFlareParticleEffect(this.x, this.y, PARTICLE_COLOR));
            vfxTimer = Random(0.3f,3f);
        }
    }

    public void render(SpriteBatch sb)
    {
        RenderImage(sb, image, tX, tY, false, false);
    }
}
