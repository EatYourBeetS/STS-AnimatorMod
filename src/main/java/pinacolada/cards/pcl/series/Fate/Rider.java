package pinacolada.cards.pcl.series.Fate;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class Rider extends PCLCard
{
    public static final PCLCardData DATA = Register(Rider.class)
            .SetSkill(2, CardRarity.COMMON)
            .SetSeriesFromClassPackage();

    public Rider()
    {
        super(DATA);

        Initialize(0, 6, 3, 3);
        SetUpgrade(0, 0, 1);

        SetAffinity_Green(1, 0, 1);
        SetAffinity_Blue(1);
        SetAffinity_Dark(1, 0, 1);

        SetAffinityRequirement(PCLAffinity.Green, 4);
        SetAffinityRequirement(PCLAffinity.Blue, 4);
    }

    @Override
    protected void OnUpgrade()
    {
        super.OnUpgrade();

        SetRetainOnce(true);
    }

    @Override
    public void OnDrag(AbstractMonster m)
    {
        super.OnDrag(m);

        if (m != null)
        {
            PCLGameUtilities.GetPCLIntent(m).AddStrength(-magicNumber);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainBlock(block);
        PCLActions.Bottom.ReduceStrength(m, magicNumber, true);
        PCLActions.Bottom.TryChooseSpendAffinity(this, PCLAffinity.Green, PCLAffinity.Blue).AddConditionalCallback(() -> {
            PCLActions.Bottom.ReduceStrength(m, secondaryValue, true);
        });
    }
}