package eatyourbeets.cards.animator.series.FullmetalAlchemist;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.powers.common.BurningPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.TargetHelper;

public class RoyMustang extends AnimatorCard
{
    public static final EYBCardData DATA = Register(RoyMustang.class)
            .SetAttack(2, CardRarity.UNCOMMON, EYBAttackType.Elemental, EYBCardTarget.ALL)
            .SetSeriesFromClassPackage();
    public static final int BURNING_ATTACK_BONUS = 15;
    public static final int BASE_BURNING = 5;

    public RoyMustang()
    {
        super(DATA);

        Initialize(4, 0, BASE_BURNING, BURNING_ATTACK_BONUS);
        SetUpgrade(4, 0, 0);

        SetAffinity_Blue(2, 0, 1);
        SetAffinity_Orange(2);
        SetAffinity_Light(1);

        SetEvokeOrbCount(1);

        SetAffinityRequirement(Affinity.Fire, 2);
    }

    @Override
    public void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        GameUtilities.ModifyMagicNumber(this, Math.max(1,BASE_BURNING - Math.max(0,GameUtilities.GetEnemies(true).size() - 1)), true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamageToAll(this, AttackEffects.FIRE);
        GameActions.Bottom.ApplyBurning(TargetHelper.Enemies(), magicNumber);

        if (CheckAffinity(Affinity.Fire))
        {
            GameActions.Bottom.Callback(() -> BurningPower.AddPlayerAttackBonus(BURNING_ATTACK_BONUS));
        }
    }
}