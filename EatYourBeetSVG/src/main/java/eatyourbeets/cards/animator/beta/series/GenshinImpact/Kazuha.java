package eatyourbeets.cards.animator.beta.series.GenshinImpact;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.powers.common.BurningPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.TargetHelper;

public class Kazuha extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Kazuha.class).SetAttack(1, CardRarity.UNCOMMON, EYBAttackType.Normal).SetSeriesFromClassPackage(true);

    public Kazuha()
    {
        super(DATA);

        Initialize(7, 0, 3, 0);
        SetUpgrade(3, 0, 1, 0);
        SetAffinity_Green(1, 0, 2);
        SetAffinity_Blue(0, 0, 1);

        SetAffinityRequirement(Affinity.Green, 9);
        SetExhaust(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealCardDamage(this, m, AttackEffects.SLASH_DIAGONAL).forEach(d -> d
                .SetDamageEffect(c -> GameEffects.List.Attack(player, c, AttackEffects.SLASH_DIAGONAL, 1.4f, 1.6f).duration));

        int poisonAmount = GameUtilities.GetPowerAmount(m, PoisonPower.POWER_ID);
        int burningAmount = GameUtilities.GetPowerAmount(m, BurningPower.POWER_ID);

        if (poisonAmount > 0) {
            for (AbstractMonster mo : GameUtilities.GetEnemies(true)) {
                if (mo != m) {
                    GameActions.Bottom.ApplyPoison(TargetHelper.Normal(mo), poisonAmount);
                }
            }
        }
        if (burningAmount > 0) {
            for (AbstractMonster mo : GameUtilities.GetEnemies(true)) {
                if (mo != m) {
                    GameActions.Bottom.ApplyBurning(TargetHelper.Normal(mo), burningAmount);
                }
            }
        }

        if (GameUtilities.GetAffinityPowerLevel(Affinity.Green) > 0 && TrySpendAffinity(Affinity.Green)) {
            GameUtilities.AddAffinityPowerUse(Affinity.Green, 1);
        }
    }
}

