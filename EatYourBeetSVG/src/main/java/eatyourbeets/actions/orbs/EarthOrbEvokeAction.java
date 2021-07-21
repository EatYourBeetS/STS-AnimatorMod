package eatyourbeets.actions.orbs;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.Hitbox;
import eatyourbeets.actions.EYBAction;
import eatyourbeets.effects.VFX;
import eatyourbeets.orbs.animator.Earth;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;

public class EarthOrbEvokeAction extends EYBAction
{
    private static final int DAMAGE_TICKS = 8;
    private float x;
    private float y;
    private float scale;


    public EarthOrbEvokeAction(int damage, float x, float y, float scale)
    {
        super(ActionType.DAMAGE);
        this.x = x;
        this.y = y;
        this.scale = scale / 2;

        Initialize(MathUtils.ceil(damage / (float) DAMAGE_TICKS));
    }

    @Override
    protected void FirstUpdate()
    {
        if (amount > 0)
        {
            CardCrawlGame.sound.play("ANIMATOR_ORB_EARTH_EVOKE", 0.1f);

            for (int i = 0; i < DAMAGE_TICKS; i++)
            {
                Hitbox hb = new Hitbox(x, y, 96, 96);
                GameActions.Top.DealDamageToRandomEnemy(amount, DamageInfo.DamageType.THORNS,
                        AttackEffect.BLUNT_LIGHT)
                        .SetOptions(true, true).SetDamageEffect(m ->
                        GameEffects.List.Add(
                                VFX.GenericThrow(hb, m.hb, Earth.imgExt1)
                                        .SetSpread(20f, 20f)
                                        .SetImageParameters(this.scale, 100f, 0f)
                                        .SetHitEffect(VFX.RockBurst(m.hb, this.scale))
                                        .SetRealtime(false)
                        ).duration);
            }
        }

        Complete();
    }
}
