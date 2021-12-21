package pinacolada.cards.pcl.series.GenshinImpact;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Frost;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class Chongyun extends PCLCard
{
    public static final PCLCardData DATA = Register(Chongyun.class).SetSkill(1, CardRarity.COMMON, eatyourbeets.cards.base.EYBCardTarget.Normal).SetSeriesFromClassPackage(true);

    public Chongyun()
    {
        super(DATA);

        Initialize(0, 5, 2);
        SetUpgrade(0, 2, 1);
        SetAffinity_Red(0,0,1);
        SetAffinity_Blue(1, 0, 0);

        SetAffinityRequirement(PCLAffinity.Blue, 4);
    }

    @Override
    public int GetXValue() {
        return magicNumber + PCLGameUtilities.GetOrbCount(Frost.ORB_ID);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainBlock(block);
        PCLActions.Bottom.ApplyFreezing(TargetHelper.Normal(m), GetXValue());

        if (TrySpendAffinity(PCLAffinity.Blue)) {
            PCLActions.Bottom.ChannelOrb(new Frost());
        }
    }
}