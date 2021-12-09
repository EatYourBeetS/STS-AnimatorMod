package eatyourbeets.cards.animator.series.FullmetalAlchemist;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.LockOnPower;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.TargetHelper;

public class RizaHawkeye extends AnimatorCard
{
    public static final EYBCardData DATA = Register(RizaHawkeye.class)
            .SetAttack(1, CardRarity.COMMON, EYBAttackType.Ranged)
            .SetSeriesFromClassPackage();

    public RizaHawkeye()
    {
        super(DATA);

        Initialize(8, 0, 2, 2);
        SetUpgrade(3, 0, 1);

        SetAffinity_Orange(1, 0, 1);
        SetAffinity_Green(1, 0, 1);
        SetAffinity_Light(1);

    }

    @Override
    protected float ModifyDamage(AbstractMonster enemy, float amount)
    {
        if (IsStarter()) {
            return super.ModifyDamage(enemy, amount + magicNumber);
        }

        return super.ModifyDamage(enemy, amount);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealCardDamage(this, m, AttackEffects.GUNSHOT);
        if (GameUtilities.GetPowerAmount(m, LockOnPower.POWER_ID) > 0 ) {
            GameActions.Bottom.ApplyWeak(TargetHelper.Normal(m), secondaryValue);
        }
        if (IsStarter()) {
            GameActions.Bottom.ApplyLockOn(TargetHelper.Normal(m), secondaryValue);
        }
    }
}