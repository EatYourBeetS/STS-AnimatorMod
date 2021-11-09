package eatyourbeets.cards.animator.enchantments;

import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.powers.common.DelayedDamagePower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.ListSelection;
import eatyourbeets.utilities.TargetHelper;

import java.util.Comparator;

public class Enchantment3 extends Enchantment
{
    public static final EYBCardData DATA = RegisterInternal(Enchantment3.class);
    public static final int INDEX = 3;
    public static final int UP5_BLOCK = 3;

    public Enchantment3()
    {
        super(DATA, INDEX);

        Initialize(0, 0, 1, 5);
    }

    @Override
    protected void OnUpgrade()
    {
        if (auxiliaryData.form == 3 || auxiliaryData.form == 4)
        {
            upgradeSecondaryValue(1);
        }
        else if (auxiliaryData.form == 5)
        {
            upgradeMagicNumber(1);
        }
    }

    @Override
    public int GetMaxUpgradeIndex()
    {
        return 5;
    }

    @Override
    protected String GetRawDescription()
    {
        if (auxiliaryData.form == 5) {
            return super.GetRawDescription(UP5_BLOCK);
        }
        return super.GetRawDescription();
    }

    @Override
    public boolean CanUsePower(int cost)
    {
        return CombatStats.Affinities.CheckAffinityLevels(Affinity.Extended(), cost, true) && (auxiliaryData.form != 5 || player.currentBlock >= UP5_BLOCK);
    }

    @Override
    public void PayPowerCost(int cost)
    {
        GameActions.Bottom.TryChooseSpendAffinity(name, cost);

        if (auxiliaryData.form == 5)
        {
            GameActions.Bottom.LoseBlock(UP5_BLOCK);
        }
    }

    @Override
    public void UsePower(AbstractMonster m)
    {
        switch (auxiliaryData.form) {
            case 0:
                GameActions.Bottom.ReduceCommonDebuffs(player, magicNumber).SetSelection(ListSelection.First(0), 1).AddCallback(po -> {
                    if (po.size() > 0) {
                        AbstractPower debuff = po.get(0);
                        if (DelayedDamagePower.POWER_ID.equals(debuff.ID)) {
                            GameActions.Bottom.ReducePower(debuff, magicNumber);
                        }
                    }
                });;
                return;
            case 1:
            case 2:
                GameActions.Bottom.ReduceCommonDebuffs(player, magicNumber).SetSelection(auxiliaryData.form == 1 ? ListSelection.First(0) : ListSelection.Last(0), 1).AddCallback(po -> {
                    if (po.size() > 0) {
                        AbstractPower debuff = po.get(0);
                        int applyAmount = magicNumber;
                        if (DelayedDamagePower.POWER_ID.equals(debuff.ID)) {
                            GameActions.Bottom.ReducePower(debuff, magicNumber);
                            applyAmount += magicNumber;
                        }

                        PowerHelper debuffHelper = null;
                        for (PowerHelper commonDebuffHelper : GameUtilities.GetCommonDebuffs()) {
                            if (commonDebuffHelper.ID.equals(debuff.ID)) {
                                debuffHelper = commonDebuffHelper;
                                break;
                            }
                        }

                        if (debuffHelper != null) {
                            for (AbstractMonster mo : GameUtilities.GetEnemies(true)) {
                                GameActions.Bottom.ApplyPower(TargetHelper.Normal(mo), debuffHelper);
                            }
                        }
                    }
                });
                return;
            case 3:
            case 4:
                GameActions.Bottom.RemoveCommonDebuffs(player, auxiliaryData.form == 3 ? ListSelection.First(0) : ListSelection.Last(0), 1);
                return;
            case 5:
                GameActions.Bottom.ReduceDebuffs(player, magicNumber)
                        .SetSelection(ListSelection.First(0), 1)
                        .Sort(Comparator.comparingInt(a -> -a.amount));
        }
    }
}