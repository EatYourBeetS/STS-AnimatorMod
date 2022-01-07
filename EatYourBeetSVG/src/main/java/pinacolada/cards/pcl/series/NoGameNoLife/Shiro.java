package pinacolada.cards.pcl.series.NoGameNoLife;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.attributes.AbstractAttribute;
import pinacolada.cards.base.attributes.TempHPAttribute;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class Shiro extends PCLCard
{
    public static final PCLCardData DATA = Register(Shiro.class)
            .SetSkill(2, CardRarity.RARE, eatyourbeets.cards.base.EYBCardTarget.None, true)
            .SetSeriesFromClassPackage();
    public static final int CHARGE_COST = 4;

    public Shiro()
    {
        super(DATA);

        Initialize(0, 0, 5, 3);
        SetCostUpgrade(-1);

        SetAffinity_Blue(1, 0, 1);
        SetAffinity_Orange(1, 0, 0);
        SetAffinity_Light(1);

        SetProtagonist(true);
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
        PCLActions.Bottom.Scry(secondaryValue).AddCallback(
                cards -> {
                    if (cards.size() > 0) {
                        PCLActions.Bottom.TriggerOrbPassive(cards.size(), true, false);
                    }

                    if (PCLGameUtilities.GetPCLAffinityPowerLevel(PCLAffinity.Blue) > 0 && PCLGameUtilities.TrySpendAffinityPower(PCLAffinity.Light, magicNumber)) {
                        PCLGameUtilities.AddAffinityPowerUse(PCLAffinity.Blue, 1);
                    }
                }
        );
    }
}