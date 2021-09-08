package eatyourbeets.cards.animator.beta.series.DateALive;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.orbs.animator.Fire;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.common.FreezingPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class KotoriItsuka extends AnimatorCard
{
    public static final EYBCardData DATA = Register(KotoriItsuka.class).SetAttack(1, CardRarity.UNCOMMON, EYBAttackType.Normal).SetSeriesFromClassPackage();
    public static final int THRESHOLD = 12;
    public static final int BURNING_ATTACK_BONUS = 20;

    public KotoriItsuka()
    {
        super(DATA);

        Initialize(8, 0, 4, 2);
        SetUpgrade(3, 0, 0);
        SetAffinity_Red(2, 0, 1);
        SetAffinity_Orange(1, 0, 0);
    }

    @Override
    protected float ModifyDamage(AbstractMonster enemy, float amount)
    {
        if (enemy != null && GameUtilities.GetPowerAmount(enemy, FreezingPower.POWER_ID) >= 1)
        {
            return super.ModifyDamage(enemy, amount * 2);
        }
        return super.ModifyDamage(enemy, amount);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.FIRE).AddCallback(m, (enemy, __) -> {
            if (GameUtilities.GetPowerAmount(enemy, FreezingPower.POWER_ID) >= 1)
            {
                GameActions.Bottom.ReducePower(player, enemy, FreezingPower.POWER_ID, magicNumber);
                GameActions.Bottom.ApplyVulnerable(player, enemy, secondaryValue);
            }
            else {
                GameActions.Bottom.ApplyBurning(player, enemy, secondaryValue);
            }

            if (isSynergizing && CombatStats.TryActivateSemiLimited(cardID)) {
                GameActions.Bottom.TriggerOrbPassive(player.orbs.size())
                        .SetFilter(o -> Fire.ORB_ID.equals(o.ID))
                        .SetSequential(true);
            }
        });
    }
}