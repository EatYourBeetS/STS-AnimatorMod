package eatyourbeets.effects.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
    protected int frame;
    protected int duration;

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
        this.duration = this.totalFrames;
        this.animTimer = animTimer;
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
        this.duration = duration;
        return this;
    }

    public void update() {
        this.animTimer -= Gdx.graphics.getDeltaTime();
        if (this.animTimer < 0.0F) {
            this.animTimer += 0.03F;
            ++this.frame;

            if (this.frame >= duration - 1) {
                this.isDone = true;
            }
        }

    }

    @Override
    protected void UpdateInternal(float deltaTime)
    {
        rotation += vR * deltaTime;

        this.animTimer -= Gdx.graphics.getDeltaTime();
        if (this.animTimer < 0.0F) {
            this.animTimer += 0.03F;
            ++this.frame;

            if (this.frame > totalFrames) {
                this.frame = totalFrames;
                this.isDone = true;
            }
        }
    }

    public void render(SpriteBatch sb) {
        sb.setColor(this.color);
        TextureRegion region = this.GetFrameRegion(this.frame);
        sb.draw(region, this.x - (this.size / 2f), this.y - (this.size / 2f), this.size / 2f, this.size / 2f, this.size, this.size, this.scale, this.scale, this.rotation);
    }

    public TextureRegion GetFrameRegion(int frame) {
        switch (this.mode) {
            case Loop:
                frame = frame % totalFrames;
            case Reverse:
                int cycle = (frame / totalFrames) % 2;
                frame = Math.abs(frame % totalFrames + (-(totalFrames - 1) * cycle));
            default:
                frame = Math.max(frame, totalFrames - 1);
        }

        float targetX = (frame % this.columns) * this.size;
        float targetY = (frame / (float) this.rows) * this.size;
        return new TextureRegion(this.imageSheet, targetX, targetY, Math.max(targetX + size, this.imageSheet.getWidth()), Math.max(targetY + size, this.imageSheet.getWidth()));
    }

    public void dispose()
    {
    }
}
