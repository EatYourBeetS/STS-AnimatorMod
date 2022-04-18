package eatyourbeets.effects.vfx.megacritCopy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.SmokeBlurEffect;
import eatyourbeets.effects.SFX;

public class SmokeBombEffect2 extends AbstractGameEffect
{
    private float x;
    private float y;

    public SmokeBombEffect2(float x, float y)
    {
        this.x = x;
        this.y = y;
        this.duration = 0.2F;
    }

    public void update()
    {
        if (this.duration == 0.2F)
        {
            SFX.Play(SFX.ATTACK_WHIFF_2, 0.95f, 1.05f, 0.9f);

            for (int i = 0; i < 90; ++i)
            {
                AbstractDungeon.effectsQueue.add(new SmokeBlurEffect(this.x, this.y));
            }
        }

        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration < 0.0F)
        {
            SFX.Play(SFX.APPEAR, 0.95f, 1.05f, 0.9f);
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
