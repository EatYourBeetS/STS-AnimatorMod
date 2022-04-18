package eatyourbeets.cards.animator.enchantments;

import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.powers.PowerTriggerCondition;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.TargetHelper;

public class Enchantment3 extends Enchantment
{
    public static final EYBCardData DATA = RegisterInternal(Enchantment3.class);
    public static final int INDEX = 3;
    public static final int UP1_POISON = 3;
    public static final int UP2_WEAK = 2;
    public static final int UP3_TAKE_DAMAGE = 4;

    public Enchantment3()
    {
        super(DATA, INDEX);

        Initialize(0, 0, 1, 1);

        requiresTarget = true;
    }

    @Override
    protected void OnUpgrade()
    {
        if (upgradeIndex == 2)
        {
            upgradeMagicNumber(+1);
            upgradeSecondaryValue(+1);
            requiresTarget = false;
            return;
        }

        requiresTarget = true;

        if (upgradeIndex == 3)
        {
            upgradeSecondaryValue(-1);
        }
    }

    @Override
    public int GetMaxUpgradeIndex()
    {
        return 3;
    }

    @Override
    public void SetUses(PowerTriggerCondition triggerCondition)
    {
        triggerCondition.SetUses(1, upgradeIndex != 2, true);
    }

    @Override
    protected String GetRawDescription()
    {
        switch (upgradeIndex)
        {
            case 1: return super.GetRawDescription(UP1_POISON);
            case 2: return super.GetRawDescription(UP2_WEAK);
            case 3: return super.GetRawDescription(UP3_TAKE_DAMAGE);
            default: return super.GetRawDescription();
        }
    }

    @Override
    public void PayPowerCost(int cost)
    {
        if (upgradeIndex == 3)
        {
            GameActions.Bottom.TakeDamageAtEndOfTurn(UP3_TAKE_DAMAGE);
        }
        else
        {
            super.PayPowerCost(cost);
        }
    }

    @Override
    public void UsePower(AbstractMonster m)
    {
        if (upgradeIndex == 2)
        {
            GameActions.Bottom.ApplyVulnerable(TargetHelper.Enemies(player), magicNumber);
            GameActions.Bottom.ApplyWeak(TargetHelper.Enemies(player), UP2_WEAK);
            return;
        }

        GameActions.Bottom.ApplyVulnerable(player, m, magicNumber);

        if (upgradeIndex == 1)
        {
            GameActions.Bottom.ApplyPoison(player, m, UP1_POISON);
        }
    }
}