package pinacolada.cards.pcl.series.Elsword;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.effects.AttackEffects;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class Ara extends PCLCard
{
    public static final PCLCardData DATA = Register(Ara.class)
            .SetAttack(1, CardRarity.COMMON)
            .SetMultiformData(2, false)
            .SetSeriesFromClassPackage();

    public Ara()
    {
        super(DATA);

        Initialize(3, 0, 1, 2);
        SetUpgrade(2, 0, 0, 0);

        SetAffinity_Green(1, 0, 1);
        SetAffinity_Red(1);

        SetAffinityRequirement(PCLAffinity.Orange, 3);
        SetHitCount(2);
    }

    @Override
    public int SetForm(Integer form, int timesUpgraded) {
        if (form == 1) {
            this.cardText.OverrideDescription(cardData.Strings.EXTENDED_DESCRIPTION[0], true);
            SetAffinityRequirement(PCLAffinity.Green, 3);
            SetAffinityRequirement(PCLAffinity.Orange, 0);
        }
        else {
            this.cardText.OverrideDescription(null, true);
            SetAffinityRequirement(PCLAffinity.Green, 0);
            SetAffinityRequirement(PCLAffinity.Orange, 3);
        }
        return super.SetForm(form, timesUpgraded);
    };

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.SPEAR).forEach(d -> d.SetSoundPitch(1.1f, 1.3f));

        if (auxiliaryData.form == 1 && TrySpendAffinity(PCLAffinity.Green))
        {
            PCLActions.Bottom.GainEndurance(magicNumber);
        }
        else if (auxiliaryData.form == 0 && TrySpendAffinity(PCLAffinity.Orange))
        {
            PCLActions.Bottom.GainVelocity(magicNumber);
        }
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (info.TryActivateSemiLimited()) {
            PCLActions.Bottom.Draw(PCLGameUtilities.GetDebuffsCount(m.powers));
            PCLActions.Bottom.DiscardFromHand(name, 1, false)
                    .SetOptions(false, false, false);
        }
    }
}