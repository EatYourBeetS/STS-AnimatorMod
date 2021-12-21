package pinacolada.effects.vfx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import eatyourbeets.effects.EYBEffect;
import pinacolada.effects.VFX;
import pinacolada.ui.TextureCache;
import pinacolada.utilities.PCLGameEffects;
import pinacolada.utilities.PCLRenderHelpers;

public class HexagonEffect extends EYBEffect
{
    private static final TextureCache image = VFX.IMAGES.Hexagon;

    protected static final int SIZE = 96;

    protected float vfxFrequency = 0.03f;
    protected Texture img;
    protected float x;
    protected float y;
    protected float vX;
    protected float vY;
    protected float vR;
    protected float flashFrequency;
    protected float vfxTimer;
    protected boolean flip;

    public HexagonEffect(float x, float y, Color color)
    {
        super(Random(0.5f, 1f));

        this.img = image.Texture();
        this.x = x - (float) (SIZE / 2);
        this.y = y - (float) (SIZE / 2);
        this.scale = 1f;
        this.vX = 1400f * Settings.scale;
        this.vR = Random(-600f, 600f);
        this.flashFrequency = this.duration * 7f;

        SetColor(color, 0.28f);
    }

    public HexagonEffect SetColor(Color color, float variance)
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

    public HexagonEffect SetFlashFrequency(float flashFrequency) {
        this.flashFrequency = flashFrequency;

        return this;
    }

    public HexagonEffect SetScale(float scale)
    {
        this.scale = scale;

        return this;
    }

    public HexagonEffect SetSpeed(float vX, float vY, float vR)
    {
        this.vX = vX;
        this.vY = vY;
        this.vR = vR;
        return this;
    }

    @Override
    protected void UpdateInternal(float deltaTime)
    {
        x += vX * deltaTime;
        y += vY * deltaTime;
        rotation += vR * deltaTime;

        if ((1f - duration) < 0.1f)
        {
            color.a = Interpolation.fade.apply(0f, 1f, (1f - duration) * 10f);
        }
        else
        {
            color.a = Interpolation.sine.apply(0.8f, 1f, this.flashFrequency);
            vfxTimer -= deltaTime;
            if (vfxTimer < 0f)
            {
                PCLGameEffects.Queue.Add(new FadingParticleEffect(this.img, this.x + SIZE * MathUtils.cos(rotation), this.y + SIZE * MathUtils.sin(rotation))
                        .SetOpacity(0.75f)
                        .SetBlendingMode(PCLRenderHelpers.BlendingMode.Glowing)
                        .SetColor(this.color.cpy())
                        .Edit(p -> p
                                .SetTargetRotation(36000f, vR)
                                .SetScale(scale * 0.75f)
                                .SetTargetScale(0f, 3f))
                        .SetDuration(1f, true));
                vfxTimer = vfxFrequency;
            }
        }

        super.UpdateInternal(deltaTime);
    }

    public void render(SpriteBatch sb)
    {
        sb.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
        sb.setColor(this.color);
        sb.draw(this.img, x, y, 0, 0, SIZE, SIZE, scale, scale, rotation, 0, 0, SIZE, SIZE, flip, false);
        sb.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
    }
}
