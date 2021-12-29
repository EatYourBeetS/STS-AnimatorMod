package pinacolada.cards.pcl.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Lightning;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.pcl.series.FullmetalAlchemist.ElricAlphonse;
import pinacolada.utilities.PCLActions;

public class ElricAlphonse_Alt extends PCLCard
{
    public static final PCLCardData DATA = Register(ElricAlphonse_Alt.class)
            .SetSkill(1, CardRarity.SPECIAL, eatyourbeets.cards.base.EYBCardTarget.Self)
            .SetSeries(ElricAlphonse.DATA.Series);

    public ElricAlphonse_Alt()
    {
        super(DATA);

        Initialize(0, 2, 3, 2);
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
        PCLActions.Bottom.ChannelOrbs(Lightning::new, secondaryValue);
        PCLActions.Bottom.GainBlock(block);
        PCLActions.Bottom.GainOrbSlots(1);

        if (info.IsSynergizing || TrySpendAffinity(PCLAffinity.Orange))
        {
            PCLActions.Bottom.GainPlatedArmor(magicNumber);
        }
    }
}