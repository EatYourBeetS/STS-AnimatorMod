package pinacolada.cards.pcl.enchantments;

import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCardData;
import pinacolada.ui.combat.PCLAffinityMeter;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLJUtils;

public class Enchantment5 extends Enchantment
{
    public static final PCLCardData DATA = RegisterInternal(Enchantment5.class);
    public static final int INDEX = 5;
    public PCLAffinity currentAffinity;

    public Enchantment5()
    {
        super(DATA, INDEX);

        Initialize(0, 0, 1, 4);
    }

    @Override
    protected void OnUpgrade()
    {
        if (auxiliaryData.form == 7) {
            upgradeMagicNumber(1);
        }
    }

    @Override
    public int GetMaxUpgradeIndex()
    {
        return 6;
    }

    @Override
    public void UsePower(AbstractMonster m)
    {
        if (!upgraded)
        {
            PCLActions.Bottom.RerollAffinity(PCLAffinityMeter.Target.CurrentAffinity)
                    .SetOptions(true, true);
            PCLActions.Bottom.RerollAffinity(PCLAffinityMeter.Target.NextAffinity)
                    .SetOptions(true, true);
            return;
        }

        currentAffinity = GetAffinity();
        if (currentAffinity != null) {
            PCLActions.Bottom.RerollAffinity(PCLAffinityMeter.Target.CurrentAffinity)
                    .SetAffinityChoices(this.currentAffinity)
                    .SetOptions(true, true);
            PCLActions.Bottom.RerollAffinity(PCLAffinityMeter.Target.NextAffinity)
                    .SetAffinityChoices(this.currentAffinity)
                    .SetOptions(true, true);
        }
        else {
            PCLAffinity af1 = PCLJUtils.Random(PCLAffinity.Basic());
            PCLAffinity af2 = PCLJUtils.Random(PCLJUtils.Filter(PCLAffinity.Extended(), a -> a != af1));
            PCLActions.Bottom.RerollAffinity(PCLAffinityMeter.Target.CurrentAffinity)
                    .SetAffinityChoices(af1, af2)
                    .SetOptions(false, true).AddCallback(newAf -> {
                        PCLActions.Bottom.RerollAffinity(PCLAffinityMeter.Target.NextAffinity)
                                .SetAffinityChoices(newAf)
                                .SetOptions(true, true);
                    });
        }
    }

    @Override
    public PCLAffinity[] GetAffinityList() {
        return GetAffinity() != null ? new PCLAffinity[] {GetAffinity()} : super.GetAffinityList();
    }

    public PCLAffinity GetAffinity()
    {
        switch (auxiliaryData.form)
        {
            case 1: return PCLAffinity.Red;
            case 2: return PCLAffinity.Green;
            case 3: return PCLAffinity.Blue;
            case 4: return PCLAffinity.Orange;
            case 5: return PCLAffinity.Light;
            case 6: return PCLAffinity.Dark;
            default: return null;
        }
    }
}