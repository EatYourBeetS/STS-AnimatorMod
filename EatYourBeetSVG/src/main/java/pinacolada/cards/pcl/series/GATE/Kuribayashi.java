package pinacolada.cards.pcl.series.GATE;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.*;
import pinacolada.effects.AttackEffects;
import pinacolada.powers.PowerHelper;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class Kuribayashi extends PCLCard
{
    public static final PCLCardData DATA = Register(Kuribayashi.class)
            .SetAttack(2, CardRarity.COMMON, PCLAttackType.Ranged)
            .SetSeriesFromClassPackage();

    public Kuribayashi()
    {
        super(DATA);

        Initialize(7, 0, 2, 3);
        SetUpgrade(3, 0, 0);

        SetAffinity_Red(1, 0, 1);
        SetAffinity_Green(1, 0, 1);
        SetAffinity_Silver(1);

        SetAffinityRequirement(PCLAffinity.Red, 3);
        SetAffinityRequirement(PCLAffinity.Green, 2);
    }

    @Override
    public void OnDrag(AbstractMonster m)
    {
        super.OnDrag(m);

        if (m != null && TrySpendAffinity(PCLAffinity.Red))
        {
            PCLGameUtilities.GetPCLIntent(m).AddStrength(-secondaryValue);
        }
    }

    @Override
    public void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        if (CheckAffinity(PCLAffinity.Green)) {
            PCLGameUtilities.IncreaseHitCount(this, 1, true);
        }
    }


    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.GUNSHOT).forEach(d -> d.SetSoundPitch(0.6f, 0.8f));
        TrySpendAffinity(PCLAffinity.Green);

        PCLActions.Bottom.ApplyVulnerable(p, m, magicNumber);

        if (TrySpendAffinity(PCLAffinity.Red))
        {
            PCLActions.Bottom.StackPower(TargetHelper.Normal(m), PowerHelper.Shackles,secondaryValue);
        }
    }
}