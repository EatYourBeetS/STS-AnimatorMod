package eatyourbeets.cards.animator.enchantments;

import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Enchantment3 extends Enchantment
{
    public static final EYBCardData DATA = RegisterInternal(Enchantment3.class);
    public static final int INDEX = 3;
    public static final int UP1_POISON = 3;
    public static final int UP2_WEAK = 1;
    public static final int UP3_TAKE_DAMAGE = 3;

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
        }
        else if (upgradeIndex == 3)
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
    public boolean CanUsePower(int cost)
    {
        return super.CanUsePower(cost) && (upgradeIndex != 3 || (GameUtilities.GetHP(player, true, false) > UP3_TAKE_DAMAGE));
    }

    @Override
    public void PayPowerCost(int cost)
    {
        super.PayPowerCost(cost);

        if (upgradeIndex == 3)
        {
            GameActions.Bottom.TakeDamage(UP3_TAKE_DAMAGE, AttackEffects.NONE);
        }
    }

    @Override
    public void UsePower(AbstractMonster m)
    {
        GameActions.Bottom.ApplyVulnerable(player, m, magicNumber);

        if (upgradeIndex == 1)
        {
            GameActions.Bottom.ApplyPoison(player, m, UP1_POISON);
        }
        else if (upgradeIndex == 2)
        {
            GameActions.Bottom.ApplyWeak(player, m, UP2_WEAK);
        }
    }
}