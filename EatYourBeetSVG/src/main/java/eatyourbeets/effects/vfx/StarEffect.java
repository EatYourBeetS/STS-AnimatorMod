package eatyourbeets.effects.vfx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import eatyourbeets.effects.EYBEffect;
import eatyourbeets.utilities.GameEffects;

public class StarEffect extends EYBEffect
{
   protected static final int SIZE = 96;
   protected Texture img;
   protected float x;
   protected float y;
   protected float vX;
   protected float vR;
   protected float vfxTimer;

    public StarEffect(float x, float y, float vxMin, float vxMax)
    {
        super(Random(0.5f, 1f));

        this.img = ImageMaster.loadImage("images/effects/Star.png");

        this.x = x - (float) (SIZE / 2);
        this.y = y - (float) (SIZE / 2);
        this.rotation = Random(5f, 10f);
        this.scale = Random(0.2f, 3.0f) * Settings.scale;
        this.vX = Random(vxMin, vxMax) * Settings.scale;
        this.vR = Random(-600f, 600f);

        if (MathUtils.randomBoolean()) this.rotation *= -1;
        SetColor();

    }

    public StarEffect SetColor()
    {
        switch (MathUtils.random(0, 5)) {
            case 0:
                this.color = new Color(1.0F, MathUtils.random(0.5F, 1.0F), 0.5F, 0.15F);
                break;
            case 1:
                this.color = new Color(MathUtils.random(0.5F, 1.0F), 1.0F, 0.5F, 0.15F);
                break;
            case 2:
                this.color = new Color(0.5F, 1.0F, MathUtils.random(0.5F, 1.0F), 0.15F);
                break;
            case 3:
                this.color = new Color(0.5F, MathUtils.random(0.5F, 1.0F), 1.0F, 0.15F);
                break;
            case 4:
                this.color = new Color(MathUtils.random(0.5F, 1.0F), 0.5F, 1.0F, 0.15F);
                break;
            default:
                this.color = new Color(1.0F, 0.5F, MathUtils.random(0.5F, 1.0F), 0.15F);
                break;
        }
        this.color.a = 0.15F;

        return this;
    }

    @Override
    protected void UpdateInternal(float deltaTime)
    {
        x += vX * deltaTime;
        rotation += vR * deltaTime;
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
            GameEffects.Queue.Add(new StarParticleEffect(x, y, color));
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
