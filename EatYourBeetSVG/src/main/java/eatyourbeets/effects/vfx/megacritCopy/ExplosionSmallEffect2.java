package eatyourbeets.effects.vfx.megacritCopy;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.DarkSmokePuffEffect;
import com.megacrit.cardcrawl.vfx.combat.SmokingEmberEffect;
import eatyourbeets.effects.SFX;
import eatyourbeets.utilities.GameEffects;

public class ExplosionSmallEffect2 extends AbstractGameEffect
{
    private static final int EMBER_COUNT = 12;
    private boolean playSFX = true;
    private float x;
    private float y;

    public ExplosionSmallEffect2(float x, float y)
    {
        this.x = x;
        this.y = y;
    }

    public ExplosionSmallEffect2 PlaySFX(boolean playSFX)
    {
        this.playSFX = playSFX;

        return this;
    }

    public void update()
    {
        GameEffects.Queue.Add(new DarkSmokePuffEffect(this.x, this.y));

        for (int i = 0; i < 12; ++i)
        {
            GameEffects.Queue.Add(new SmokingEmberEffect(this.x + MathUtils.random(-50.0F, 50.0F) * Settings.scale, this.y + MathUtils.random(-50.0F, 50.0F) * Settings.scale));
        }

        if (playSFX)
        {
            SFX.Play(SFX.ATTACK_FIRE, 0.8f, 0.9f);
        }

        this.isDone = true;
    }

    public void render(SpriteBatch sb)
    {
    }

    public void dispose()
    {
    }
}
