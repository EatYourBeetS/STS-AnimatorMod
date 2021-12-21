package pinacolada.effects.vfx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.core.Settings;
import eatyourbeets.effects.EYBEffect;
import eatyourbeets.interfaces.delegates.ActionT1;
import eatyourbeets.interfaces.delegates.ActionT2;
import pinacolada.effects.AnimatedProjectile;
import pinacolada.effects.Projectile;
import pinacolada.utilities.PCLRenderHelpers;

public class GenericAnimationEffect extends EYBEffect
{
    public final AnimatedProjectile projectile;

    protected boolean fade;
    protected int endFrame;
    protected float vR;
    protected float x;
    protected float y;

    public GenericAnimationEffect(Texture texture, float x, float y, int rows, int columns)
    {
        this(texture,x,y,rows,columns,0.03f);
    }

    public GenericAnimationEffect(Texture texture, float x, float y, int rows, int columns, float frameDuration)
    {
        super(Settings.ACTION_DUR_MED, false);

        this.projectile = new AnimatedProjectile(texture, rows, columns, frameDuration);
        this.projectile.SetPosition(x, y).SetTargetPosition(x, y);
        this.endFrame = this.projectile.totalFrames;
        this.scale = 1;
        this.x = x;
        this.y = y;
    }

    public GenericAnimationEffect SetFrame(int frame)
    {
        this.projectile.frame = frame;

        return this;
    }

    public GenericAnimationEffect SetColor(Color color)
    {
        this.projectile.SetColor(this.color = color.cpy());

        return this;
    }

    public GenericAnimationEffect SetRotation(float startRotation)
    {
        this.projectile.SetRotation(startRotation);

        return this;
    }

    public GenericAnimationEffect SetScale(float scale)
    {
        this.projectile.SetScale(this.scale = scale);

        return this;
    }

    public GenericAnimationEffect SetTargetRotation(float targetRotation, float rotationSpeed)
    {
        this.projectile.SetTargetRotation(targetRotation, rotationSpeed);

        return this;
    }

    public GenericAnimationEffect SetTargetScale(float scale, float growthRate)
    {
        this.projectile.SetTargetScale(scale, growthRate);

        return this;
    }

    public GenericAnimationEffect SetMode(AnimatedProjectile.AnimationMode mode, int duration)
    {
        this.projectile.mode = mode;
        this.endFrame = duration;

        return this;
    }

    public GenericAnimationEffect SetFading(int fadingFrames)
    {
        this.endFrame = projectile.totalFrames + fadingFrames;
        this.fade = fadingFrames > 0;

        return this;
    }

    public GenericAnimationEffect SetBlendingMode(PCLRenderHelpers.BlendingMode blendingMode)
    {
        this.projectile.SetBlendingMode(blendingMode);

        return this;
    }

    public GenericAnimationEffect Edit(ActionT1<AnimatedProjectile> action)
    {
        action.Invoke(projectile);

        return this;
    }

    public <T> GenericAnimationEffect Edit(T state, ActionT2<T, Projectile> action)
    {
        action.Invoke(state, projectile);

        return this;
    }

    @Override
    protected void UpdateInternal(float deltaTime)
    {
        this.projectile.Update(deltaTime);

        if (this.projectile.frame >= endFrame)
        {
            Complete();
            return;
        }

        if (fade && this.projectile.frame >= this.projectile.totalFrames)
        {
            this.projectile.color.a = Interpolation.fade.apply(0f, 1f, (endFrame - projectile.frame) / (float)(endFrame - projectile.totalFrames));
        }
    }

    @Override
    public void render(SpriteBatch sb)
    {
        if (projectile != null)
        {
            projectile.Render(sb);
        }
    }

    @Override
    public void dispose()
    {

    }
}
