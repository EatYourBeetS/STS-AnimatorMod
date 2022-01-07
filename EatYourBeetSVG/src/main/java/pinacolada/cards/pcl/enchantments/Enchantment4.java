package pinacolada.cards.pcl.enchantments;

import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCardData;
import pinacolada.powers.PCLCombatStats;
import pinacolada.powers.affinity.AbstractPCLAffinityPower;
import pinacolada.utilities.PCLActions;

public class Enchantment4 extends Enchantment
{
    public static final PCLCardData DATA = RegisterInternal(Enchantment4.class);
    public static final int INDEX = 4;
    public PCLAffinity currentAffinity;

    public Enchantment4()
    {
        super(DATA, INDEX);

        Initialize(0, 0, 1, 3);
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
            PCLActions.Bottom.GainRandomAffinityPower(magicNumber, true);
            return;
        }

        currentAffinity = GetAffinity();
        if (currentAffinity != null) {
            final AbstractPCLAffinityPower p = PCLCombatStats.MatchingSystem.GetPower(currentAffinity);
            if (p != null) {
                PCLActions.Bottom.StackAffinityPower(currentAffinity, magicNumber);
            }
            else {
                PCLActions.Bottom.GainEnergyNextTurn(magicNumber);
            }
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