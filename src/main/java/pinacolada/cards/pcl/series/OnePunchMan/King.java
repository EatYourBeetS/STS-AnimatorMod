package pinacolada.cards.pcl.series.OnePunchMan;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class King extends PCLCard
{
    public static final PCLCardData DATA = Register(King.class)
            .SetSkill(0, CardRarity.COMMON, eatyourbeets.cards.base.EYBCardTarget.None)
            .SetSeriesFromClassPackage()
            .SetMultiformData(2, false);

    public King()
    {
        super(DATA);

        Initialize(0, 1, 3, 3);
        SetUpgrade(0, 1, 0, 0);

        SetAffinity_Red(1, 0, 1);
    }

    @Override
    public int SetForm(Integer form, int timesUpgraded) {
        SetInnate(upgraded && form == 0);
        return super.SetForm(form, timesUpgraded);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();
        PCLActions.Bottom.ApplyVulnerable(TargetHelper.RandomEnemy(player), 1).IgnoreArtifact(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainBlock(block);
        if (PCLGameUtilities.TrySpendAffinityPower(PCLAffinity.Light, magicNumber)) {
            PCLActions.Bottom.GainMight(magicNumber);
        }
    }
}