package eatyourbeets.actions.orbs;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.Hitbox;
import eatyourbeets.actions.EYBAction;
import eatyourbeets.effects.VFX;
import eatyourbeets.orbs.animator.Earth;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;

import static eatyourbeets.effects.vfx.RotatingRocksEffect.GetCosVariance;
import static eatyourbeets.effects.vfx.RotatingRocksEffect.GetSinVariance;

public class EarthOrbEvokeAction extends EYBAction
{
    private static final int DAMAGE_TICKS = 8;
    private float x;
    private float y;
    private float scale;
    private float spread;
    private float baseDuration;


    public EarthOrbEvokeAction(int damage, float x, float y, float scale)
    {
        super(ActionType.DAMAGE);
        this.x = x;
        this.y = y;
        this.scale = scale;
        this.spread = 0;
        this.baseDuration = duration;

        Initialize(MathUtils.ceil(damage / (float) DAMAGE_TICKS));
    }

    @Override
    protected void UpdateInternal(float deltaTime)
    {
        spread = Interpolation.exp5Out.apply(0f, 64f, (this.baseDuration - this.duration) * 8);
    }

    @Override
    protected void FirstUpdate()
    {
        if (amount > 0)
        {
            CardCrawlGame.sound.play("ANIMATOR_ORB_EARTH_EVOKE", 0.1f);

            for (int i = 0; i < DAMAGE_TICKS; i++)
            {
                Hitbox hb = new Hitbox(x-48 + GetCosVariance(spread, i), y-48 + GetSinVariance(spread, i), 96, 96);
                GameActions.Top.DealDamageToRandomEnemy(amount, DamageInfo.DamageType.THORNS,
                        AttackEffect.NONE)
                        .SetOptions(true, true).SetDamageEffect(m ->
                {
                    GameEffects.List.Add(
                            VFX.GenericThrow(hb, m.hb, Earth.imgExt1)
                                    .SetSpread(20f, 20f)
                                    .SetImageParameters(this.scale * MathUtils.random(0.7f,0.8f), MathUtils.random(400f,600f), MathUtils.random(0f,600f))
                                    .SetHitEffect(VFX.RockBurst(m.hb, this.scale))
                    );
                    return 0f;
                });
            }
        }

        Complete();
    }
}
