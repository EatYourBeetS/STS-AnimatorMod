package eatyourbeets.cards.animator.enchantments;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.RetainCardPower;
import eatyourbeets.cards.base.AffinityType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;

public class Enchantment2 extends Enchantment
{
    public static final EYBCardData DATA = RegisterInternal(Enchantment2.class);
    public static final int INDEX = 2;
    public static final int UP5_LOSE_HP = 4;

    public Enchantment2()
    {
        super(DATA, INDEX);

        Initialize(0, 0, 0, 1);
        SetUpgrade(0, 0, 0, 1);
    }

    @Override
    protected void OnUpgrade()
    {
        switch (upgradeIndex)
        {
            case 1: upgradeMagicNumber(3); break;
            case 2: upgradeMagicNumber(1); break;
            case 3: upgradeMagicNumber(1); break;
            case 4: upgradeMagicNumber(1); break;
            case 5: upgradeMagicNumber(UP5_LOSE_HP * 2); break;
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
        return upgradeIndex == 5 ? super.GetRawDescription(UP5_LOSE_HP) : super.GetRawDescription();
    }

    @Override
    public boolean CanUsePower(int cost)
    {
        return super.CanUsePower(cost) && (upgradeIndex != 5 || (player.currentHealth > magicNumber));
    }

    @Override
    public void PayPowerCost(int cost)
    {
        super.PayPowerCost(cost);

        if (upgradeIndex == 5)
        {
            GameActions.Bottom.LoseHP(UP5_LOSE_HP, AbstractGameAction.AttackEffect.NONE).IgnoreTempHP(true);
        }
    }

    @Override
    public void UsePower(AbstractMonster m)
    {
        if (!upgraded)
        {
            for (AffinityType t : AffinityType.BasicTypes())
            {
                CombatStats.Affinities.GetPower(t).RetainOnce();
            }
            return;
        }

        final AffinityType type = GetAffinityType();
        GameActions.Bottom.StackAffinityPower(type, 1, true);
        switch (type)
        {
            case Red:
                GameActions.Bottom.GainPlatedArmor(magicNumber);
                break;

            case Green:
                GameActions.Bottom.StackPower(new RetainCardPower(player, magicNumber));
                break;

            case Blue:
                GameActions.Bottom.GainOrbSlots(magicNumber);
                break;

            case Light:
                GameActions.Bottom.GainTemporaryArtifact(magicNumber);
                break;

            case Dark:
                GameActions.Bottom.GainTemporaryHP(magicNumber);
                break;
        }
    }

    public AffinityType GetAffinityType()
    {
        switch (upgradeIndex)
        {
            case 1: return AffinityType.Red;
            case 2: return AffinityType.Green;
            case 3: return AffinityType.Blue;
            case 4: return AffinityType.Light;
            case 5: return AffinityType.Dark;
            default: return null;
        }
    }
}