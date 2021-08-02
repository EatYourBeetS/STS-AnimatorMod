package eatyourbeets.effects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import eatyourbeets.utilities.RenderHelpers;

public class AnimatedProjectile extends Projectile
{
    public enum AnimationMode
    {
        Fade,
        Loop,
        Remain,
        Reverse
    }

    protected final int columns;
    protected final int rows;
    protected float animTimer;
    protected float animDelay;
    public AnimationMode mode;
    public int frame;
    public final int totalFrames;

    public AnimatedProjectile(Texture texture, float size)
    {
        this(texture, size, size, 0.03F, Integer.MAX_VALUE);
    }

    public AnimatedProjectile(Texture texture, float size, float animTimer)
    {
        this(texture, size, size, animTimer, Integer.MAX_VALUE);
    }

    public AnimatedProjectile(Texture texture, float size, float animTimer, int totalFrames)
    {
        this(texture, size, size, animTimer, totalFrames);
    }

    public AnimatedProjectile(Texture texture, float width, float height, float animTimer, int totalFrames)
    {
        super(texture, width, height);

        this.frame = 0;
        this.mode = AnimationMode.Remain;

        this.columns = MathUtils.ceil(texture.getWidth() / width);
        this.rows = MathUtils.ceil(texture.getHeight() / height);
        this.totalFrames = Math.min(totalFrames, this.rows * this.columns);
        this.animTimer = this.animDelay = animTimer;
    }

    @Override
    public void Update(float delta)
    {
        super.Update(delta);

        this.animTimer -= delta;
        if (this.animTimer < 0.0F)
        {
            this.animTimer += this.animDelay;
            ++this.frame;
        }
    }

    @Override
    public void Render(SpriteBatch sb)
    {
        this.Render(sb, color == null ? sb.getColor() : color);
    }

    @Override
    public void Render(SpriteBatch sb, Color color)
    {
        TextureRegion region = this.GetFrameRegion(this.frame);
        RenderHelpers.DrawCentered(sb, color, region, current_pos.x + current_offset.x, current_pos.y + current_offset.y,
                width, height, scale, current_pos.z + current_offset.z);
    }

    public TextureRegion GetFrameRegion(int frame)
    {
        int zframe;
        switch (this.mode)
        {
            case Loop:
                zframe = frame % totalFrames;
                break;
            case Reverse:
                int cycle = (frame / totalFrames) % 2;
                zframe = Math.abs(frame % totalFrames + (-(totalFrames - 1) * cycle));
                break;
            default:
                zframe = Math.min(frame, totalFrames - 1);
                break;
        }

        int targetX = (zframe % this.columns) * (int) this.width;
        int targetY = zframe / this.rows * (int) this.height;
        return new TextureRegion(this.texture, targetX, targetY, (int) width, (int) height);
    }

    public void dispose()
    {
    }
}
