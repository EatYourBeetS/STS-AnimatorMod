package eatyourbeets.effects.vfx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import eatyourbeets.effects.EYBEffect;
import eatyourbeets.effects.SFX;
import eatyourbeets.effects.VFX;
import eatyourbeets.ui.TextureCache;
import eatyourbeets.utilities.GameEffects;

public class StrongPunchEffect extends EYBEffect
{
    public static final TextureCache image = IMAGES.Punch;

    protected float x;
    protected float y;
    protected float rotationSpeed = 500f;
    protected float vfxTimer = 1;
    protected float baseScale;
    protected boolean triggered = false;

    public StrongPunchEffect(float x, float y, float baseScale)
    {
        super(1f);

        this.x = x;
        this.y = y;
        this.scale = this.baseScale = Math.max(baseScale,1);
        this.color = Color.WHITE.cpy();
    }

    @Override
    protected void UpdateInternal(float deltaTime)
    {

        if ((1f - duration) < 0.1f)
        {
            color.a = Interpolation.fade.apply(0.1f, 1f, (1f - duration) * 7f);
        }
        else
        {
            color.a = Interpolation.pow2Out.apply(0.1f, 1f, duration);
        }

        vfxTimer -= deltaTime / duration;
        if (vfxTimer < 0f)
        {
            if (!triggered) {
                GameEffects.Queue.Add(VFX.Whack(x, y).SetColor(Color.FIREBRICK)).SetScale(this.baseScale);
                SFX.Play(SFX.BLUNT_HEAVY,0.5f,0.6f);
            }
            else {
                x += Interpolation.sine.apply(-5f, 5f, this.duration * 50);
                y += Interpolation.sine.apply(-5f, 5f, this.duration * 50);
            }
        }
        else {
            this.rotation += rotationSpeed * deltaTime / duration;
            this.scale = Interpolation.linear.apply(this.baseScale, 1, duration);
        }

        super.UpdateInternal(deltaTime);
    }

    public void render(SpriteBatch sb)
    {
        RenderImage(sb, image.Texture(), x, y, false, false);
    }
}
