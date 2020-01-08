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
    public void render(SpriteBatch spriteBatch)
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
                tickDuration();
            }
        }
        else
        {
            tickDuration();
        }
    }

    @Override
    public void dispose()
    {

    }

    protected void FirstUpdate()
    {

    }

    protected void UpdateInternal()
    {
        tickDuration();
    }

    protected void Complete()
    {
        this.isDone = true;
    }

    protected void tickDuration()
    {
        if (isRealtime)
        {
            this.duration -= Gdx.graphics.getRawDeltaTime();
        }
        else
        {
            this.duration -= Gdx.graphics.getDeltaTime();
        }

        if (this.duration < 0.0F)
        {
            this.isDone = true;
        }
    }
}
