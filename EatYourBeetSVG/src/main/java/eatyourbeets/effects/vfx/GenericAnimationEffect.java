package eatyourbeets.effects.vfx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import eatyourbeets.effects.EYBEffect;
import eatyourbeets.resources.GR;

import java.util.ArrayList;

public abstract class GenericAnimationEffect extends EYBEffect
{
    public enum AnimationMode
    {
        Fade,
        Loop,
        Remain,
        Reverse
    }

    protected Texture imageSheet;
    protected ArrayList<TextureRegion> regions;
    protected AnimationMode mode;
    protected final float size;
    protected final int columns;
    protected final int rows;
    protected final int totalFrames;
    protected float x;
    protected float y;
    protected float vR;
    protected float animTimer;
    protected float animDelay;
    protected int frame;
    protected int animDuration;

    public GenericAnimationEffect(String imgUrl, float x, float y, float size, float animTimer) {
        this.x = x;
        this.y = y;
        this.frame = 0;
        this.scale = Settings.scale;
        this.color = Color.WHITE.cpy();
        this.color.a = 1;
        this.mode = AnimationMode.Remain;

        this.imageSheet = GR.GetTexture(imgUrl);
        this.size = size;
        this.columns = MathUtils.ceil(imageSheet.getWidth() / size);
        this.rows = MathUtils.ceil(imageSheet.getHeight() / size);
        this.totalFrames = this.rows * this.columns;
        this.animDuration = this.totalFrames;
        this.animTimer = this.animDelay = animTimer;
    }


    public GenericAnimationEffect(String imgUrl, float x, float y, float size) {
        this(imgUrl,x,y,size,0.03F);
    }

    public GenericAnimationEffect SetColor(Color color){
        this.color = color;
        return this;
    }

    public GenericAnimationEffect SetImageParameters(float scale, float vR, float rotation)
    {
        this.scale = scale;
        this.vR = vR;
        this.rotation = rotation;

        return this;
    }

    public GenericAnimationEffect SetMode(AnimationMode mode, int duration){
        this.mode = mode;
        this.animDuration = duration;
        return this;
    }

    @Override
    protected void UpdateInternal(float deltaTime)
    {
        rotation += vR * deltaTime;

        this.animTimer -= deltaTime;
        if (this.animTimer < 0.0F) {
            this.animTimer += this.animDelay;
            ++this.frame;

            if (this.frame >= animDuration) {
                Complete();
            }
        }

        if (this.mode == AnimationMode.Fade && this.frame >= totalFrames) {
            this.color.a = Interpolation.fade.apply(0f, 1f, (animDuration - this.frame) / (float)(animDuration - totalFrames));
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setColor(this.color);
        TextureRegion region = this.GetFrameRegion(this.frame);
        sb.draw(region, this.x - (this.size / 2f), this.y - (this.size / 2f), this.size / 2f, this.size / 2f, this.size, this.size, this.scale, this.scale, this.rotation);
    }

    public TextureRegion GetFrameRegion(int frame) {
        int zframe;
        switch (this.mode) {
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

        int targetX = (zframe % this.columns) * (int)this.size;
        int targetY = zframe / this.rows * (int)this.size;
        return new TextureRegion(this.imageSheet, targetX, targetY, (int)size, (int)size);
    }

    public void dispose()
    {
    }
}
