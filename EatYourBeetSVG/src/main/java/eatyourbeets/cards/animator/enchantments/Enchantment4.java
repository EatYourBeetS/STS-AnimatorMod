package eatyourbeets.cards.animator.enchantments;

import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.PowerTriggerCondition;
import eatyourbeets.utilities.GameActions;

public class Enchantment4 extends Enchantment
{
    public static final EYBCardData DATA = RegisterInternal(Enchantment4.class);
    public static final int INDEX = 4;

    public Enchantment4()
    {
        super(DATA, INDEX);

        Initialize(0, 0, 0, 1);
        SetUpgrade(0, 0, 0, 1);
    }

    @Override
    public int GetMaxUpgradeIndex()
    {
        return 5;
    }

    @Override
    public void SetUses(PowerTriggerCondition triggerCondition)
    {
        triggerCondition.SetUses(1, upgraded, true);
    }

    @Override
    public boolean CanUsePower(int cost)
    {
        return upgradeIndex > 0 ? CombatStats.Affinities.GetAffinityLevel(GetAffinity()) >= secondaryValue : super.CanUsePower(cost);
    }

    @Override
    public void PayPowerCost(int cost)
    {
        if (upgradeIndex >= 1)
        {
            CombatStats.Affinities.TryUseAffinity(GetAffinity(), secondaryValue);
            secondaryValue += 1;
            isSecondaryValueModified = true;
        }
        else
        {
            super.PayPowerCost(cost);
        }
    }

    @Override
    public void UsePower(AbstractMonster m)
    {
        if (!upgraded)
        {
            for (Affinity a : Affinity.Basic())
            {
                GameActions.Bottom.StackAffinityPower(a, 1, true);
            }
            return;
        }

        final Affinity affinity = GetAffinity();
        GameActions.Bottom.StackAffinityPower(affinity, 1, true);
    }

    public Affinity GetAffinity()
    {
        switch (upgradeIndex)
        {
            case 1: return Affinity.Red;
            case 2: return Affinity.Green;
            case 3: return Affinity.Blue;
            case 4: return Affinity.Light;
            case 5: return Affinity.Dark;
            default: return null;
        }
    }
}
