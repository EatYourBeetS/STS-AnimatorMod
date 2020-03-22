package eatyourbeets.actions.orbs;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.random.Random;
import eatyourbeets.actions.EYBAction;
import eatyourbeets.utilities.GameActions;

public class EarthOrbEvokeAction extends EYBAction
{
    private static final int DAMAGE_TICKS = 8;

    public EarthOrbEvokeAction(int damage)
    {
        super(ActionType.DAMAGE);

        Initialize((int)Math.ceil(damage / (float)DAMAGE_TICKS));
    }

    @Override
    protected void FirstUpdate()
    {
        if (amount > 0)
        {
            CardCrawlGame.sound.play("ANIMATOR_ORB_EARTH_EVOKE", 0.1f);

            Random rng = AbstractDungeon.cardRandomRng;
            DamageInfo.DamageType damageType = DamageInfo.DamageType.THORNS;

            for (int i = 0; i < DAMAGE_TICKS; i++)
            {
                GameActions.Top.DealDamageToRandomEnemy(amount, damageType, rng.randomBoolean() ? AttackEffect.SMASH : AttackEffect.BLUNT_LIGHT)
                .SetOptions(true, true);
            }
        }

        Complete();
    }
}
