package pinacolada.cards.pcl.series.MadokaMagica;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.*;
import pinacolada.effects.AttackEffects;
import pinacolada.orbs.pcl.Fire;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class SuzuneAmano extends PCLCard
{
    public static final PCLCardData DATA = Register(SuzuneAmano.class)
            .SetAttack(1, CardRarity.UNCOMMON, PCLAttackType.Fire)
            .SetSeriesFromClassPackage();

    public SuzuneAmano()
    {
        super(DATA);

        Initialize(7, 0, 3, 6);
        SetUpgrade(3, 0, 1, 0);

        SetAffinity_Red(1);
        SetAffinity_Blue(1, 0, 1);
        SetAffinity_Dark(1);

        SetAffinityRequirement(PCLAffinity.Red, 2);
    }

    @Override
    public int GetXValue() {
        return magicNumber + PCLGameUtilities.GetOrbCount(Fire.ORB_ID);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (info.IsSynergizing) {
            PCLActions.Bottom.Draw(1);
        }

        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.FIRE);

        if (TrySpendAffinity(PCLAffinity.Red))
        {
            PCLActions.Bottom.ExhaustFromHand(name, 1, false)
                    .ShowEffect(true, true)
                    .SetOptions(false, false, false)
                    .AddCallback(m, (enemy, cards) ->
                    {
                        if (cards != null && cards.size() > 0)
                        {
                            PCLActions.Bottom.ApplyBurning(TargetHelper.Normal(enemy), GetXValue());
                        }
                    });
        }
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {

    }
}