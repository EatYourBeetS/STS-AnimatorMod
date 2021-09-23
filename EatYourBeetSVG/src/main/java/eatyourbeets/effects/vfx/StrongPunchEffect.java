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

    protected boolean triggered;
    protected float baseScale;
    protected float vfxTimer;
    protected float x;
    protected float y;

    public StrongPunchEffect(float x, float y)
    {
        super(1f, true);

        this.x = x;
        this.y = y;
        this.scale = this.baseScale = 1f;
        this.color = Color.WHITE.cpy();
        this.vfxTimer = startingDuration;
    }

    public StrongPunchEffect SetScale(float scale)
    {
        this.baseScale = scale;

        return this;
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
            if (triggered)
            {
                x += Interpolation.sine.apply(-25f, 25f, duration * 50);
                y += Interpolation.sine.apply(-25f, 25f, duration * 50);
            }
            else
            {
                GameEffects.Queue.Add(VFX.Whack(x, y).SetColor(Color.SCARLET)).SetScale(scale);
                SFX.Play(SFX.ANIMATOR_PUNCH, 0.7f, 0.8f);
                triggered = true;
            }
        }
        else
        {
            this.scale = Interpolation.linear.apply(baseScale * 0.5f, baseScale, duration);
        }

        super.UpdateInternal(deltaTime);
    }

    public void render(SpriteBatch sb)
    {
        RenderImage(sb, image.Texture(), x, y, false, false);
    }
}
