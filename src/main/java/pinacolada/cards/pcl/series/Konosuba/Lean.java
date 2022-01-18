package pinacolada.cards.pcl.series.Konosuba;

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
import pinacolada.utilities.PCLJUtils;

public class Lean extends PCLCard
{
    public static final PCLCardData DATA = Register(Lean.class)
            .SetSkill(1, CardRarity.UNCOMMON, eatyourbeets.cards.base.EYBCardTarget.None, true)
            .SetMultiformData(2, false)
            .SetSeriesFromClassPackage();

    public Lean()
    {
        super(DATA);

        Initialize(0, 0, 2, 3);

        SetAffinity_Blue(1, 0, 1);
        SetAffinity_Green(1);

        SetAffinityRequirement(PCLAffinity.Blue, 4);
        SetAffinityRequirement(PCLAffinity.Green, 4);

        SetExhaust(true);
    }

    @Override
    public int SetForm(Integer form, int timesUpgraded) {
        if (timesUpgraded > 0) {
            if (form == 1) {
                SetHaste(false);
                SetRetain(true);
            }
            else {
                SetHaste(true);

            }
        }
        return super.SetForm(form, timesUpgraded);
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return TempHPAttribute.Instance.SetCard(this, true);
    }

    @Override
    public int GetXValue() {
        return secondaryValue * PCLJUtils.Count(player.hand.group, PCLGameUtilities::HasBlueAffinity);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainTemporaryHP(magicNumber);
        int amount = GetXValue();
        if (amount > 0) {
            PCLActions.Bottom.GainSupportDamage(amount);
        }

        if (TrySpendAffinity(PCLAffinity.Blue)) {
            PCLActions.Bottom.ChannelOrbs(PCLGameUtilities::GetRandomCommonOrb, 1);
        }
        if (TrySpendAffinity(PCLAffinity.Green)) {
            PCLActions.Bottom.ChannelOrbs(PCLGameUtilities::GetRandomCommonOrb, 1);
        }
    }
}