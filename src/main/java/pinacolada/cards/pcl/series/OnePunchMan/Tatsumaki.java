package pinacolada.cards.pcl.series.OnePunchMan;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.orbs.pcl.Air;
import pinacolada.stances.WisdomStance;
import pinacolada.utilities.PCLActions;

public class Tatsumaki extends PCLCard
{
    public static final PCLCardData DATA = Register(Tatsumaki.class)
            .SetSkill(2, CardRarity.UNCOMMON, eatyourbeets.cards.base.EYBCardTarget.None)
            .SetSeriesFromClassPackage();

    public Tatsumaki()
    {
        super(DATA);

        Initialize(0, 3, 2);
        SetUpgrade(0, 1, 1);

        SetAffinity_Red(1, 0, 0);
        SetAffinity_Blue(1, 0, 1);
        SetAffinity_Light(1);
        SetAffinity_Green(0,0,1);

        SetEvokeOrbCount(1);
        SetAffinityRequirement(PCLAffinity.Red, 3);
        SetAffinityRequirement(PCLAffinity.Blue, 3);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainBlock(block);
        PCLActions.Bottom.ChannelOrb(new Air());
        PCLActions.Bottom.GainFocus(magicNumber, true);
        if (IsStarter() && TrySpendAffinity(PCLAffinity.Red, PCLAffinity.Blue)) {
            PCLActions.Bottom.ChangeStance(WisdomStance.STANCE_ID);
        }
    }
}