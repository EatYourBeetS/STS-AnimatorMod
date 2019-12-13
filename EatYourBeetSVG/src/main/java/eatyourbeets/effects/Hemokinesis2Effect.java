package eatyourbeets.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class Hemokinesis2Effect extends AbstractGameEffect
{
    private final float x;
    private final float y;
    private final float tX;
    private final float tY;

    public Hemokinesis2Effect(float x, float y, float targetX, float targetY)
    {
        this.x = x;
        this.y = y;
        this.tX = targetX;
        this.tY = targetY;
        this.scale = 0.12F;
        this.duration = 0.5F;
    }

    public void update()
    {
        this.scale -= Gdx.graphics.getDeltaTime();
        if (this.scale < 0.0F)
        {
            AbstractDungeon.effectsQueue.add(new HemokinesisParticle2Effect(this.x + MathUtils.random(60.0F, -60.0F) * Settings.scale, this.y + MathUtils.random(60.0F, -60.0F) * Settings.scale, this.tX, this.tY, this.x > this.tX));
            this.scale = 0.04F;
        }

        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration < 0.0F)
        {
            this.isDone = true;
        }

    }

    public void render(SpriteBatch sb)
    {
    }

    public void dispose()
    {
    }
}
