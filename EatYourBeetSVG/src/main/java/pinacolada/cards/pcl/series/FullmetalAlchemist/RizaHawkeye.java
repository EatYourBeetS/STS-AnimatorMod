package pinacolada.cards.pcl.series.FullmetalAlchemist;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.LockOnPower;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAttackType;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.effects.AttackEffects;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class RizaHawkeye extends PCLCard
{
    public static final PCLCardData DATA = Register(RizaHawkeye.class)
            .SetAttack(1, CardRarity.COMMON, PCLAttackType.Ranged)
            .SetSeriesFromClassPackage();

    public RizaHawkeye()
    {
        super(DATA);

        Initialize(8, 0, 2, 2);
        SetUpgrade(3, 0, 1);

        SetAffinity_Orange(1, 0, 1);
        SetAffinity_Green(1, 0, 1);

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
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.GUNSHOT);
        if (PCLGameUtilities.GetPowerAmount(m, LockOnPower.POWER_ID) > 0 ) {
            PCLActions.Bottom.ApplyWeak(TargetHelper.Normal(m), secondaryValue);
        }
        if (IsStarter()) {
            PCLActions.Bottom.ApplyLockOn(TargetHelper.Normal(m), secondaryValue);
        }
    }
}