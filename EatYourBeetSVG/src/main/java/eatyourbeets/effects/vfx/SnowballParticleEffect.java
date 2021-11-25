package eatyourbeets.effects.vfx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.core.Settings;
import eatyourbeets.effects.EYBEffect;
import eatyourbeets.ui.TextureCache;
import eatyourbeets.utilities.RandomizedList;

public class SnowballParticleEffect extends EYBEffect
{
    private static final TextureCache[] images = {IMAGES.FrostSnow1, IMAGES.FrostSnow2, IMAGES.FrostSnow3, IMAGES.FrostSnow4};
    private static final RandomizedList<TextureCache> textures = new RandomizedList<>();

    protected static final float GRAVITY = 180f * Settings.scale;
    protected static final int SIZE = 96;

    protected Texture img;
    protected float floor;
    protected float x;
    protected float y;
    protected float vX;
    protected float vY;
    protected float vR;
    protected boolean flip;
    protected boolean enableFloor = true;

    public SnowballParticleEffect(float x, float y, Color color)
    {
        super(Random(0.5f, 1f));

        this.img = RandomElement(images, textures).Texture();
        this.x = x - (float) (SIZE / 2);
        this.y = y - (float) (SIZE / 2);
        this.rotation = Random(-10f, 10f);
        this.scale = Random(0.2f, 1.5f) * Settings.scale;
        this.vX = Random(-650f, 650f) * Settings.scale;
        this.vY = Random(-650f, 650f) * Settings.scale;
        this.floor = Random(100f, 250f) * Settings.scale;
        this.vR = Random(-600f, 600f);
        this.flip = RandomBoolean(0.5f);

        SetColor(color);
    }

    public SnowballParticleEffect SetColor(Color color, float variance)
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

    public SnowballParticleEffect SetScale(float scale)
    {
        this.scale = scale;

        return this;
    }

    public SnowballParticleEffect SetSpeed(float vX, float vY)
    {
        this.vX = vX;
        this.vY = vY;
        return this;
    }

    public SnowballParticleEffect DisableFloor()
    {
        enableFloor = false;
        return this;
    }

    @Override
    protected void UpdateInternal(float deltaTime)
    {
        vY += GRAVITY / scale * deltaTime;
        x += vX * deltaTime;
        y += vY * deltaTime;
        rotation += vR * deltaTime;
        if (scale > 0.3f * Settings.scale)
        {
            scale -= deltaTime * 2f;
        }

        if (enableFloor && y < floor)
        {
            vY = -vY * 0.35f;
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
        sb.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
        sb.setColor(this.color);
        sb.draw(this.img, x, y, SIZE * 0.5f, SIZE * 0.5f, SIZE, SIZE, scale, scale, rotation, 0, 0, SIZE, SIZE, flip, false);
        sb.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
    }
}
