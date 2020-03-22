package eatyourbeets.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public abstract class EYBEffect extends AbstractGameEffect
{
    protected boolean isRealtime;
    protected final AbstractPlayer player;
    protected int ticks;

    public EYBEffect()
    {
        this(Settings.ACTION_DUR_FAST);
    }

    public EYBEffect(float duration)
    {
        this(duration, false);
    }

    public EYBEffect(float duration, boolean isRealtime)
    {
        this.isRealtime = isRealtime;
        this.duration = this.startingDuration = duration;
        this.player = AbstractDungeon.player;
    }

    public EYBEffect SetRealtime(boolean isRealtime)
    {
        this.isRealtime = isRealtime;

        return this;
    }

    public EYBEffect SetDuration(float duration, boolean isRealtime)
    {
        this.isRealtime = isRealtime;
        this.duration = this.startingDuration = duration;

        return this;
    }

    public EYBEffect AddDuration(float duration, boolean isRealtime)
    {
        this.isRealtime = isRealtime;
        this.duration = (this.startingDuration += duration);

        return this;
    }

    public EYBEffect SetColor(Color color)
    {
        this.color = color;

        return this;
    }

    @Override
    public void render(SpriteBatch sb)
    {

    }

    @Override
    public void update()
    {
        if (duration == startingDuration)
        {
            FirstUpdate();

            if (!this.isDone)
            {
                TickDuration(GetDeltaTime());
            }
        }
        else
        {
            UpdateInternal(GetDeltaTime());
        }
    }

    @Override
    public void dispose()
    {

    }

    protected void FirstUpdate()
    {

    }

    protected void UpdateInternal(float deltaTime)
    {
        if (TickDuration(deltaTime))
        {
            Complete();
        }
    }

    protected void Complete()
    {
        this.isDone = true;
    }

    protected boolean TickDuration(float deltaTime)
    {
        this.ticks += 1;
        this.duration -= deltaTime;

        if (this.duration < 0f && ticks >= 3) // ticks are necessary for SuperFastMode at 1000% speed
        {
            this.isDone = true;
        }

        return isDone;
    }

    protected float GetDeltaTime()
    {
        return isRealtime ? Gdx.graphics.getRawDeltaTime() : Gdx.graphics.getDeltaTime();
    }

    protected void RenderImage(SpriteBatch sb, TextureAtlas.AtlasRegion img, float x, float y)
    {
        sb.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
        sb.setColor(this.color);
        sb.draw(img, x, y, img.packedWidth * 0.5f, img.packedHeight * 0.5f, img.packedWidth, img.packedHeight, scale, scale, rotation);
        sb.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
    }

    protected static int Random(int min, int max)
    {
        return MathUtils.random(min, max);
    }

    protected static float Random(float min, float max)
    {
        return MathUtils.random(min, max);
    }

    protected static boolean RandomBoolean(float chance)
    {
        return MathUtils.randomBoolean(chance);
    }
}
