package eatyourbeets.effects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import eatyourbeets.utilities.RenderHelpers;

public class AnimatedProjectile extends Projectile
{
    public enum AnimationMode
    {
        Loop,
        Remain,
        Reverse
    }

    public AnimationMode mode;
    public int totalFrames;
    public int frame;

    protected TextureRegion region;
    protected final int columns;
    protected final int rows;
    protected float frameTimer;
    protected float frameDelay;

    private static int GetCellSize(int totalWidth, int cells)
    {
        if (totalWidth % cells != 0)
        {
            throw new RuntimeException("The texture can't be evenly divided");
        }

        return totalWidth / cells;
    }

    public AnimatedProjectile(Texture texture, int rows, int columns)
    {
        this(texture, rows, columns, 0.03F);
    }

    public AnimatedProjectile(Texture texture, int rows, int columns, float frameDuration)
    {
        this(texture, rows, columns, frameDuration, rows * columns);
    }

    public AnimatedProjectile(Texture texture, int rows, int columns, float frameDuration, int maxFrames)
    {
        super(texture, GetCellSize(texture.getWidth(), columns), GetCellSize(texture.getHeight(), rows));

        this.totalFrames = Math.min(maxFrames, rows * columns);
        this.frameTimer = this.frameDelay = frameDuration;
        this.mode = AnimationMode.Remain;
        this.columns = columns;
        this.rows = rows;
        this.frame = 0;
    }

    @Override
    public void Update(float delta)
    {
        super.Update(delta);

        this.frameTimer -= delta;
        if (this.frameTimer < 0f)
        {
            this.frame += 1;
            this.frameTimer = this.frameDelay;
            this.region = null;
        }
    }

    @Override
    public void Render(SpriteBatch sb, Color color, float cX, float cY, float scale)
    {
        if (region == null)
        {
            this.region = GetFrameRegion(frame);
        }

        RenderHelpers.DrawCentered(sb, color, region, cX, cY, width, height, scale, GetCurrentRotation(true), flipX, flipY);
    }

    public TextureRegion GetFrameRegion(int frame)
    {
        final int clampedFrame;
        if (mode == AnimationMode.Reverse)
        {
            final int cycle = (frame / totalFrames) % 2;
            clampedFrame = Math.abs((frame % totalFrames) - ((totalFrames - 1) * cycle));
        }
        else
        {
            clampedFrame = mode == AnimationMode.Loop ? (frame % totalFrames) : Math.min(frame, totalFrames - 1);
        }

        final int h = (int) height;
        final int w = (int) width;
        return new TextureRegion(texture, (clampedFrame % columns) * w, (clampedFrame / columns) * h, w, h);
    }
}
