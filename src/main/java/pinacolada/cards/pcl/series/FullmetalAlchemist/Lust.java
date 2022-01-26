package pinacolada.cards.pcl.series.FullmetalAlchemist;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.*;
import pinacolada.effects.AttackEffects;
import pinacolada.utilities.PCLActions;

public class Lust extends PCLCard
{
    public static final PCLCardData DATA = Register(Lust.class)
            .SetSkill(1, CardRarity.COMMON, PCLCardTarget.AoE)
            .SetSeriesFromClassPackage();

    public Lust()
    {
        super(DATA);

        Initialize(0, 0, 5, 2);
        SetUpgrade(0, 0, 0, 0);

        SetAffinity_Dark(1, 0, 0);
        SetAffinity_Orange(1, 0, 0);

        SetAffinityRequirement(PCLAffinity.Dark, 5);
    }

    @Override
    public void OnUpgrade() {
        SetRetainOnce(true);
        SetAffinityRequirement(PCLAffinity.Dark, 4);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        int amount = TrySpendAffinity(PCLAffinity.Dark) ? secondaryValue + 1 : secondaryValue;
        PCLActions.Top.ApplyFrail(TargetHelper.Enemies(), amount);
        PCLActions.Bottom.ApplyVulnerable(TargetHelper.Enemies(), amount);
        PCLActions.Bottom.ApplyWeak(TargetHelper.Enemies(), amount);
        PCLActions.Bottom.DealDamageAtEndOfTurn(p, p, magicNumber, AttackEffects.SLASH_VERTICAL);
    }
}