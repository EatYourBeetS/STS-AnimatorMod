package pinacolada.cards.pcl.series.RozenMaiden;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.attributes.AbstractAttribute;
import pinacolada.cards.base.attributes.TempHPAttribute;
import pinacolada.stances.pcl.InvocationStance;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class Hinaichigo extends PCLCard
{
    public static final PCLCardData DATA = Register(Hinaichigo.class)
    		.SetSkill(1, CardRarity.COMMON).SetSeriesFromClassPackage();

    public Hinaichigo()
    {
        super(DATA);

        Initialize(0, 0, 2, 6);
        SetUpgrade(0, 0, 0, 1);

        SetAffinity_Light(1, 0, 0);

        SetAffinityRequirement(PCLAffinity.Blue, 4);
    }

    @Override
    protected void OnUpgrade()
    {
        SetAffinityRequirement(PCLAffinity.Blue, 3);
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
        if (PCLGameUtilities.GetPowerAmount(m, PoisonPower.POWER_ID) > 0) {
            PCLActions.Bottom.ApplyWeak(p, m, magicNumber);
        }
        else {
            PCLActions.Bottom.ApplyPoison(p, m, secondaryValue);
        }

        if (IsStarter() && TrySpendAffinity(PCLAffinity.Blue)) {
            PCLActions.Bottom.ChangeStance(InvocationStance.STANCE_ID);
        }
    }
}
