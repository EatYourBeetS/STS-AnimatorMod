package pinacolada.cards.pcl.series.OwariNoSeraph;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.utilities.PCLActions;

public class Shinoa extends PCLCard
{
    public static final PCLCardData DATA = Register(Shinoa.class)
            .SetSkill(1, CardRarity.COMMON, PCLCardTarget.AoE)
            .SetSeriesFromClassPackage();

    public Shinoa()
    {
        super(DATA);

        Initialize(0, 6, 1);
        SetUpgrade(0, 3, 0);

        SetAffinity_Red(1);
        SetAffinity_Green(1, 0, 1);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        PCLActions.Bottom.ApplyWeak(TargetHelper.Enemies(), magicNumber);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainBlock(block);
        PCLActions.Bottom.ApplyVulnerable(TargetHelper.Enemies(), magicNumber);

        if (info.IsSynergizing)
        {
            PCLActions.Bottom.ApplyWeak(TargetHelper.Enemies(), magicNumber);
        }
    }
}