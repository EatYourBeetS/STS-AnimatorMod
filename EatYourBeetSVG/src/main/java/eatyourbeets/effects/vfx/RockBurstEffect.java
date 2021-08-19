package eatyourbeets.effects.vfx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.Hitbox;
import eatyourbeets.effects.EYBEffect;
import eatyourbeets.effects.SFX;
import eatyourbeets.effects.VFX;
import eatyourbeets.orbs.animator.Earth;
import eatyourbeets.utilities.Colors;
import eatyourbeets.utilities.GameEffects;

public class RockBurstEffect extends EYBEffect
{
    public static final int PROJECTILES = 25;
    public static final float RADIUS = 240;

    public Hitbox hb;

    public RockBurstEffect(Hitbox hb, float scale)
    {
        super(1f, true);

        this.hb = hb;
        this.scale = scale;
        this.color = Color.WHITE.cpy();
    }

    @Override
    protected void FirstUpdate()
    {
        SFX.Play(scale > 1 ? SFX.BLUNT_HEAVY : SFX.BLUNT_FAST, 0.9f, 1.1f);
        GameEffects.Queue.Add(VFX.Whack(hb, 0).SetScale(0.25f * Settings.scale).SetColor(Color.TAN));

        for (int i = 0; i < PROJECTILES; ++i)
        {
            float angle = Random(-500f, 500f);
            GameEffects.Queue.Add(new FadingParticleEffect(Earth.GetRandomTexture(), hb.cX, hb.cY)
            .Edit(angle, (r, p) -> p
                    .SetColor(Colors.Random(0.7f, 1f, true))
                    .SetScale(scale * Random(0.2f, 0.5f)).SetTargetRotation(36000)
                    .SetSpeed(Random(300f, 420f), Random(300f, 420f), Random(500f, 800f))
                    .SetTargetPosition(hb.cX + RADIUS * MathUtils.cos(r), hb.cY + RADIUS * MathUtils.sin(r))));
        }

        Complete();
    }
}
