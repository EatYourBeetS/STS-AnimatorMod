package pinacolada.effects.vfx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import eatyourbeets.effects.EYBEffect;

public class FireBurstParticleEffect extends EYBEffect
{
   protected static final float GRAVITY = 180f * Settings.scale;
   protected AtlasRegion img;
   protected float floor;
   protected float x;
   protected float y;
   protected float vX;
   protected float vY;

    public FireBurstParticleEffect(float x, float y, Color color)
    {
        super(Random(0.5f, 1f));

        int roll = Random(0, 2);
        if (roll == 0)
        {
            this.img = ImageMaster.TORCH_FIRE_1;
        }
        else if (roll == 1)
        {
            this.img = ImageMaster.TORCH_FIRE_2;
        }
        else
        {
            this.img = ImageMaster.TORCH_FIRE_3;
        }

        this.x = x - (float) (this.img.packedWidth / 2);
        this.y = y - (float) (this.img.packedHeight / 2);
        this.rotation = Random(-10f, 10f);
        this.scale = Random(2f, 4f);
        this.vX = Random(-900f, 900f) * Settings.scale;
        this.vY = Random(0f, 500f) * Settings.scale;
        this.floor = Random(100f, 250f) * Settings.scale;

        SetColor(color);
    }

    public FireBurstParticleEffect SetColor(Color color, float variance)
    {
        this.color = color.cpy();
        this.color.a = 0;

        if (variance > 0)
        {
            this.color.r = Math.max(0, color.r - Random(0, variance));
            this.color.g = Math.max(0, color.g - Random(0, variance));
            this.color.b = Math.max(0, color.b - Random(0, variance));
        }

        return this;
    }

    @Override
    protected void UpdateInternal(float deltaTime)
    {
        vY += GRAVITY / scale * deltaTime;
        x += vX * deltaTime * MathUtils.sinDeg(deltaTime);
        y += vY * deltaTime;
        if (scale > 0.3f)
        {
            scale -= deltaTime * 2f;
        }

        if (y < floor)
        {
            vY = -vY * 0.75f;
            y = floor + 0.1f;
            vX *= 1.1f;
        }

        if ((1f - duration) < 0.1f)
        {
            color.a = Interpolation.fade.apply(0f, 1f, (1f - duration) * 10f);
        }
        else
        {
            color.a = Interpolation.pow2Out.apply(0f, 1f, duration);
        }

        super.UpdateInternal(deltaTime);
    }

    public void render(SpriteBatch sb)
    {
        RenderImage(sb, img, x, y);
    }
}
