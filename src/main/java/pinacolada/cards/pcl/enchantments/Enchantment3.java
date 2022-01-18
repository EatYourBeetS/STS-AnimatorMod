package pinacolada.cards.pcl.enchantments;

import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import eatyourbeets.utilities.ListSelection;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.PCLCardData;
import pinacolada.powers.PCLPowerHelper;
import pinacolada.powers.common.DelayedDamagePower;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

import java.util.Comparator;

public class Enchantment3 extends Enchantment
{
    public static final PCLCardData DATA = RegisterInternal(Enchantment3.class);
    public static final int INDEX = 3;
    public static final int UP5_BLOCK = 3;

    public Enchantment3()
    {
        super(DATA, INDEX);

        Initialize(0, 0, 1, 6);
    }

    @Override
    protected void OnUpgrade()
    {
        if (auxiliaryData.form == 3 || auxiliaryData.form == 4)
        {
            upgradeSecondaryValue(4);
        }
        else if (auxiliaryData.form == 5)
        {
            upgradeMagicNumber(6);
        }
    }

    @Override
    public int GetMaxUpgradeIndex()
    {
        return 5;
    }

    @Override
    public void UsePower(AbstractMonster m)
    {
        switch (auxiliaryData.form) {
            case 0:
                PCLActions.Bottom.ReduceCommonDebuffs(player, magicNumber).SetSelection(ListSelection.First(0), 1).AddCallback(po -> {
                    if (po.size() > 0) {
                        AbstractPower debuff = po.get(0);
                        if (DelayedDamagePower.POWER_ID.equals(debuff.ID)) {
                            PCLActions.Bottom.ReducePower(debuff, magicNumber);
                        }
                    }
                });
                return;
            case 1:
            case 2:
                PCLActions.Bottom.ReduceCommonDebuffs(player, magicNumber).SetSelection(auxiliaryData.form == 1 ? ListSelection.First(0) : ListSelection.Last(0), 1).AddCallback(po -> {
                    if (po.size() > 0) {
                        AbstractPower debuff = po.get(0);
                        int applyAmount = magicNumber;
                        if (DelayedDamagePower.POWER_ID.equals(debuff.ID)) {
                            PCLActions.Bottom.ReducePower(debuff, magicNumber);
                            applyAmount += magicNumber;
                        }

                        PCLPowerHelper debuffHelper = null;
                        for (PCLPowerHelper commonDebuffHelper : PCLGameUtilities.GetPCLCommonDebuffs()) {
                            if (commonDebuffHelper.ID.equals(debuff.ID)) {
                                debuffHelper = commonDebuffHelper;
                                break;
                            }
                        }

                        if (debuffHelper != null) {
                            for (AbstractMonster mo : PCLGameUtilities.GetEnemies(true)) {
                                PCLActions.Bottom.ApplyPower(TargetHelper.Normal(mo), debuffHelper);
                            }
                        }
                    }
                });
                return;
            case 3:
            case 4:
                PCLActions.Bottom.RemoveCommonDebuffs(player, auxiliaryData.form == 3 ? ListSelection.First(0) : ListSelection.Last(0), 1);
                return;
            case 5:
                PCLActions.Bottom.ReduceDebuffs(player, magicNumber)
                        .SetSelection(ListSelection.First(0), 1)
                        .Sort(Comparator.comparingInt(a -> -a.amount));
        }
    }
}