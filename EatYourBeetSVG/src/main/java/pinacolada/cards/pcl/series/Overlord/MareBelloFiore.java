package pinacolada.cards.pcl.series.Overlord;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.attributes.AbstractAttribute;
import pinacolada.cards.base.attributes.TempHPAttribute;
import pinacolada.orbs.pcl.Earth;
import pinacolada.utilities.PCLActions;

public class MareBelloFiore extends PCLCard
{
    public static final PCLCardData DATA = Register(MareBelloFiore.class)
            .SetSkill(2, CardRarity.UNCOMMON, eatyourbeets.cards.base.EYBCardTarget.None)
            .SetSeriesFromClassPackage();

    public MareBelloFiore()
    {
        super(DATA);

        Initialize(0, 0, 3, 2);
        SetUpgrade(0, 0, 2, 1);

        SetAffinity_Blue(1);
        SetAffinity_Green(1);

        SetAffinityRequirement(PCLAffinity.Blue, 2);
        SetAffinityRequirement(PCLAffinity.Green, 2);

        SetExhaust(true);
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return TempHPAttribute.Instance.SetCard(this, true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainTemporaryHP(magicNumber);

        if (TrySpendAffinity(PCLAffinity.Blue, PCLAffinity.Green))
        {
            PCLActions.Bottom.ApplyWeak(TargetHelper.Enemies(), secondaryValue);
        }

        PCLActions.Bottom.ChannelOrb(new Earth());
        PCLActions.Bottom.TriggerOrbPassive(player.orbs.size())
        .SetFilter(o -> Earth.ORB_ID.equals(o.ID))
        .SetSequential(true);
    }
}