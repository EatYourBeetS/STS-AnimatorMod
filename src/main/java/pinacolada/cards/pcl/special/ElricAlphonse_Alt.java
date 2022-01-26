package pinacolada.cards.pcl.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.*;
import pinacolada.cards.pcl.series.FullmetalAlchemist.ElricAlphonse;
import pinacolada.orbs.PCLOrbHelper;
import pinacolada.utilities.PCLActions;

public class ElricAlphonse_Alt extends PCLCard
{
    public static final PCLCardData DATA = Register(ElricAlphonse_Alt.class)
            .SetSkill(2, CardRarity.SPECIAL, PCLCardTarget.Self)
            .SetSeries(ElricAlphonse.DATA.Series);

    public ElricAlphonse_Alt()
    {
        super(DATA);

        Initialize(0, 7, 3, 2);
        SetUpgrade(0, 3, 0, 0);

        SetAffinity_Blue(1);
        SetAffinity_Orange(1);
        SetAffinity_Red(1);

        SetExhaust(true);

        SetAffinityRequirement(PCLAffinity.Orange, 4);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.ChannelOrbs(PCLOrbHelper.Lightning, secondaryValue);
        PCLActions.Bottom.GainBlock(block);
        PCLActions.Bottom.GainOrbSlots(1);

        if (info.IsSynergizing || TrySpendAffinity(PCLAffinity.Orange))
        {
            PCLActions.Bottom.GainPlatedArmor(magicNumber);
        }
    }
}