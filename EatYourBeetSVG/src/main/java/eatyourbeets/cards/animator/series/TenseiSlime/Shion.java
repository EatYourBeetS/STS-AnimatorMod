package eatyourbeets.cards.animator.series.TenseiSlime;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.powers.common.BurningPower;
import eatyourbeets.powers.replacement.AnimatorVulnerablePower;
import eatyourbeets.powers.replacement.AnimatorWeakPower;
import eatyourbeets.stances.ForceStance;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Shion extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Shion.class)
            .SetAttack(2, CardRarity.COMMON)
            .SetSeriesFromClassPackage();

    public Shion()
    {
        super(DATA);

        Initialize(15, 0, 2, 9);
        SetUpgrade(3, 0, 0);

        SetAffinity_Red(1, 1, 2);
    }

    @Override
    protected float ModifyDamage(AbstractMonster enemy, float amount)
    {
        BurningPower enemyBurning = GameUtilities.GetPower(enemy, BurningPower.class);
        AnimatorVulnerablePower enemyVulnerable = GameUtilities.GetPower(enemy, AnimatorVulnerablePower.class);
        AnimatorWeakPower playerWeak = GameUtilities.GetPower(player, AnimatorWeakPower.class);
        if (enemyBurning != null) {
            amount = BurningPower.CalculateDamage(amount, enemyBurning.GetMultiplier());
        }
        if (enemyVulnerable != null) {
            amount = AnimatorVulnerablePower.CalculateDamage(amount, enemyVulnerable.GetMultiplier());
        }
        if (playerWeak != null) {
            amount = AnimatorWeakPower.CalculateDamage(amount, playerWeak.GetMultiplier());
        }
        return super.ModifyDamage(enemy, amount);
    }


    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.BLUNT_HEAVY);

        if (info.IsSynergizing && info.TryActivateSemiLimited())
        {
            GameActions.Bottom.ChangeStance(ForceStance.STANCE_ID);
        }
    }
}