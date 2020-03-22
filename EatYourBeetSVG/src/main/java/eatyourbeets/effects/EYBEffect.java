package eatyourbeets.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public abstract class EYBEffect extends AbstractGameEffect
{
    protected boolean isRealtime;
    protected final AbstractPlayer player;
    protected int amount;
    protected int ticks;

    public EYBEffect()
    {
        this(0);
    }

    public EYBEffect(int amount)
    {
        this(amount, Settings.ACTION_DUR_FAST);
    }

    public EYBEffect(int amount, float duration)
    {
        this(amount, duration, false);
    }

    public EYBEffect(float duration, boolean isRealtime)
    {
        this(0, duration, isRealtime);
    }

    public EYBEffect(int amount, float duration, boolean isRealtime)
    {
        this.amount = amount;
        this.isRealtime = isRealtime;
        this.duration = this.startingDuration = duration;
        this.player = AbstractDungeon.player;
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
}
