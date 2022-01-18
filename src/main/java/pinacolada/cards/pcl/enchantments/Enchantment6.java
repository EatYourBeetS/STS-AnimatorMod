package pinacolada.cards.pcl.enchantments;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.pcl.special.*;
import pinacolada.utilities.PCLActions;

public class Enchantment6 extends Enchantment
{
    public static final PCLCardData DATA = RegisterInternal(Enchantment6.class);
    public static final int INDEX = 6;

    public Enchantment6()
    {
        super(DATA, INDEX);

        Initialize(0, 0, 1, 12);

        refreshEachTurn = false;
    }

    @Override
    protected void OnUpgrade()
    {
        if (auxiliaryData.form == 7) {
            upgradeSecondaryValue(5);
        }
    }

    @Override
    public int GetMaxUpgradeIndex()
    {
        return 7;
    }

    @Override
    public void UsePower(AbstractMonster m)
    {
        OrbCore core = GetOrbCore();
        if (core != null) {
            PCLActions.Bottom.MakeCardInHand(core);
        }
        else {
            PCLActions.Bottom.Add(OrbCore.SelectCoreAction(name, 1)
                            .SetOptions(true, false)
                            .AddCallback(cards ->
                            {
                                for (AbstractCard c : cards)
                                {
                                    if (auxiliaryData.form == 7) {
                                        PCLActions.Bottom.PlayCard(c, null);
                                    }
                                    else {
                                        PCLActions.Bottom.MakeCardInHand(c);
                                    }
                                }
                            }));
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

    protected OrbCore GetOrbCore() {
        switch(auxiliaryData.form) {
            case 1:
                return new OrbCore_Fire();
            case 2:
                return new OrbCore_Air();
            case 3:
                return new OrbCore_Frost();
            case 4:
                return new OrbCore_Earth();
            case 5:
                return new OrbCore_Lightning();
            case 6:
                return new OrbCore_Dark();
        }
        return null;
    }
}