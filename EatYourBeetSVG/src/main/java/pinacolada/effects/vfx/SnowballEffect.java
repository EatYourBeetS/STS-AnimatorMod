package pinacolada.effects.vfx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.combat.FrostOrbActivateEffect;
import com.megacrit.cardcrawl.vfx.combat.FrostOrbPassiveEffect;
import com.megacrit.cardcrawl.vfx.combat.LightFlareParticleEffect;
import pinacolada.effects.PCLEffect;
import pinacolada.utilities.PCLGameEffects;

public class SnowballEffect extends PCLEffect
{
    protected Color secondaryColor;
    protected float x;
    protected float y;
    protected float startX;
    protected float startY;
    protected float targetX;
    protected float targetY;
    protected float vfxTimer;

    public SnowballEffect(float startX, float startY, float targetX, float targetY)
    {
        super(0.5f);

        this.color = Color.SKY.cpy();
        this.secondaryColor = color.cpy();
        this.startX = startX;
        this.startY = startY;
        this.targetX = targetX + MathUtils.random(-20f, 20f) * Settings.scale;
        this.targetY = targetY + MathUtils.random(-20f, 20f) * Settings.scale;
        this.x = startX;
        this.y = startY;
    }

    public SnowballEffect SetColor(Color mainColor, Color secondaryColor)
    {
        this.color = mainColor.cpy();
        this.secondaryColor = secondaryColor.cpy();

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
            PCLGameEffects.Queue.Add(new LightFlareParticleEffect(x, y, secondaryColor));
            PCLGameEffects.Queue.Add(new FrostOrbPassiveEffect(x, y));
            if (RandomBoolean(0.3f)) {
                PCLGameEffects.Queue.Add(new SnowballParticleEffect(this.x, this.y, color).SetScale(Random(0.05f,0.1f) * Settings.scale));
            }
        }

        if (TickDuration(deltaTime))
        {
            PCLGameEffects.Queue.Add(new SnowballImpactEffect(x, y).SetColor(color)).SetRealtime(isRealtime);
            PCLGameEffects.Queue.Add(new FrostOrbActivateEffect(x, y));
            Complete();
        }
    }
}
