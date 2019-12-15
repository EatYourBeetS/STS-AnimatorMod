package eatyourbeets.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public abstract class EYBEffect extends AbstractGameEffect
{
    protected final AbstractPlayer player;
    protected int amount;

    public EYBEffect()
    {
        this(0);
    }

    public EYBEffect(int amount)
    {
        this(amount, 0);
    }

    public EYBEffect(int amount, float duration)
    {
        this.amount = amount;
        this.duration = this.startingDuration = duration;
        this.player = AbstractDungeon.player;
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
        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration < 0.0F)
        {
            Complete();
        }
    }
}
