package eatyourbeets.effects.vfx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.combat.LightFlareParticleEffect;
import eatyourbeets.effects.EYBEffect;
import eatyourbeets.utilities.GameEffects;

public class FireballEffect extends EYBEffect
{
    protected Color secondaryColor;
    protected float x;
    protected float y;
    protected float startX;
    protected float startY;
    protected float targetX;
    protected float targetY;
    protected float vfxTimer;

    public FireballEffect(float startX, float startY, float targetX, float targetY)
    {
        super(0.5f);

        this.color = Color.CHARTREUSE;
        this.secondaryColor = color.cpy();
        this.startX = startX;
        this.startY = startY;
        this.targetX = targetX + MathUtils.random(-20f, 20f) * Settings.scale;
        this.targetY = targetY + MathUtils.random(-20f, 20f) * Settings.scale;
        this.x = startX;
        this.y = startY;
    }

    public FireballEffect SetColor(Color mainColor, Color secondaryColor)
    {
        this.color = mainColor;
        this.secondaryColor = secondaryColor;

        return this;
    }

    @Override
    protected void UpdateInternal(float deltaTime)
    {
        x = Interpolation.fade.apply(targetX, startX, duration / startingDuration);
        y = Interpolation.fade.apply(targetY, startY, duration / startingDuration);

        vfxTimer -= deltaTime;
        if (vfxTimer < 0f)
        {
            vfxTimer = 0.016f;
            GameEffects.Queue.Add(new LightFlareParticleEffect(x, y, color));
            GameEffects.Queue.Add(new FireBurstParticleEffect(x, y, secondaryColor)).SetRealtime(isRealtime);
        }

        if (TickDuration(deltaTime))
        {
            GameEffects.Queue.Add(new GhostIgniteEffect(x, y).SetColor(color)).SetRealtime(isRealtime);
            GameEffects.Queue.Add(new GhostlyWeakFireEffect(x, y).SetColor(secondaryColor)).SetRealtime(isRealtime);
        }
    }
}
