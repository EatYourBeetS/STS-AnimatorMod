package eatyourbeets.actions.orbs;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import eatyourbeets.actions.EYBAction;
import eatyourbeets.utilities.GameActions;

public class EarthOrbEvokeAction extends EYBAction
{
    private static final int DAMAGE_TICKS = 8;

    public EarthOrbEvokeAction(int damage)
    {
        super(ActionType.DAMAGE);

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
                GameActions.Top.DealDamageToRandomEnemy(amount, DamageInfo.DamageType.THORNS,
                rng.randomBoolean() ? AttackEffect.SMASH : AttackEffect.BLUNT_LIGHT)
                .SetOptions(true, true);
            }
        }

        Complete();
    }
}
