package eatyourbeets.cards.animator.series.Katanagatari;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.stances.NeutralStance;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;

public class ZankiKiguchi extends AnimatorCard
{
    public static final EYBCardData DATA = Register(ZankiKiguchi.class)
            .SetAttack(0, CardRarity.COMMON)
            .SetSeriesFromClassPackage();

    public ZankiKiguchi()
    {
        super(DATA);

        Initialize(2, 0);
        SetUpgrade(1, 0);

        SetAffinity_Red(1);
        SetAffinity_Green(0, 0, 1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.BLUNT_HEAVY);

        final int force = CombatStats.Affinities.GetPowerAmount(Affinity.Red);
        final int agility = CombatStats.Affinities.GetPowerAmount(Affinity.Green);
        if (force != agility)
        {
            GameActions.Bottom.StackAffinityPower(force > agility ? Affinity.Green : Affinity.Red, 1, upgraded);
        }

        if (CombatStats.CanActivateSemiLimited(cardID))
        {
            GameActions.Bottom.ChangeStance(NeutralStance.STANCE_ID)
            .AddCallback(stance ->
            {
                if (stance != null && !stance.ID.equals(NeutralStance.STANCE_ID) && CombatStats.TryActivateSemiLimited(cardID))
                {
                    GameActions.Bottom.GainInspiration(1);
                }
            });
        }
    }
}