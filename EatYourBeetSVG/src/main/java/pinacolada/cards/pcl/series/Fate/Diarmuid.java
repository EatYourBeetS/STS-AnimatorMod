package pinacolada.cards.pcl.series.Fate;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.*;
import pinacolada.effects.AttackEffects;
import pinacolada.powers.PCLCombatStats;
import pinacolada.ui.combat.PCLAffinityMeter;
import pinacolada.utilities.PCLActions;

public class Diarmuid extends PCLCard
{
    public static final PCLCardData DATA = Register(Diarmuid.class)
            .SetAttack(1, CardRarity.COMMON, PCLAttackType.Piercing)
            .SetSeriesFromClassPackage(true);

    public Diarmuid()
    {
        super(DATA);

        Initialize(6, 0, 2);
        SetUpgrade(0, 0, 1);

        SetAffinity_Red(1, 0, 1);
        SetAffinity_Green(1, 0, 1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.SLASH_VERTICAL);
        if (PCLCombatStats.MatchingSystem.AffinityMeter.GetNextAffinity() == PCLAffinity.Red)
        {
            PCLActions.Bottom.ApplyVulnerable(TargetHelper.Normal(m), magicNumber);
        }
        else {
            PCLActions.Bottom.ApplyShackles(TargetHelper.Normal(m), magicNumber);
        }

        if (!info.IsSynergizing) {
            PCLActions.Bottom.RerollAffinity(PCLAffinityMeter.Target.NextAffinity)
                    .SetAffinityChoices(PCLAffinity.Red)
                    .SetOptions(true, true);
        }
    }
}