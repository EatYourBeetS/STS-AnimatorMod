package pinacolada.cards.pcl.enchantments;

import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCardData;
import pinacolada.powers.PowerTriggerConditionType;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class Enchantment2 extends Enchantment
{
    public static final PCLCardData DATA = RegisterInternal(Enchantment2.class);
    public static final int INDEX = 2;
    public PCLAffinity currentAffinity;

    public Enchantment2()
    {
        super(DATA, INDEX);

        Initialize(0, 0, 3, 1);

        triggerConditionType = PowerTriggerConditionType.Energy;
    }

    @Override
    protected void OnUpgrade()
    {
    }

    @Override
    public int GetMaxUpgradeIndex()
    {
        return 7;
    }

    @Override
    public void UsePower(AbstractMonster m)
    {
        currentAffinity = GetAffinity();
        if (currentAffinity == null) {
            currentAffinity = PCLGameUtilities.GetRandomElement(PCLAffinity.Basic());
        }
        PCLActions.Bottom.AddAffinity(currentAffinity, magicNumber);

        if (auxiliaryData.form == 7) {
            PCLActions.Bottom.AddAffinity(PCLGameUtilities.GetRandomElement(PCLAffinity.Extended()), magicNumber);
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