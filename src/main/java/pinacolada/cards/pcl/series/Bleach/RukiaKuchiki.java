package pinacolada.cards.pcl.series.Bleach;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.*;
import pinacolada.cards.pcl.special.RukiaBankai;
import pinacolada.orbs.PCLOrbHelper;
import pinacolada.powers.PCLCombatStats;
import pinacolada.utilities.PCLActions;

public class RukiaKuchiki extends PCLCard
{
    public static final PCLCardData DATA = Register(RukiaKuchiki.class).SetSkill(1, CardRarity.UNCOMMON, PCLCardTarget.None).SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(new RukiaBankai(), false));

    public RukiaKuchiki()
    {
        super(DATA);

        Initialize(0, 2, 2, 10);
        SetUpgrade(0, 3, 0, 0);
        SetAffinity_Green(1, 0, 2);
        SetAffinity_Blue(1, 0, 0);

        SetAffinityRequirement(PCLAffinity.Green, 3);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainBlock(block);

        if (IsStarter())
        {
            PCLActions.Bottom.ChannelOrbs(PCLOrbHelper.Frost, magicNumber);
        }

        if (CheckSpecialCondition(true))
        {
            PCLActions.Bottom.MakeCardInDrawPile(new RukiaBankai());
            PCLActions.Last.Exhaust(this);
        }
    }

    @Override
    public boolean CheckSpecialCondition(boolean tryUse){
        return PCLCombatStats.MatchingSystem.GetPowerAmount(PCLAffinity.Green) >= secondaryValue || PCLCombatStats.MatchingSystem.GetPowerAmount(PCLAffinity.Blue) >= secondaryValue;
    }
}