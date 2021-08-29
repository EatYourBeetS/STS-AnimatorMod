package eatyourbeets.cards.animator.enchantments;

import eatyourbeets.effects.AttackEffects;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.affinity.AbstractAffinityPower;
import eatyourbeets.powers.replacement.TemporaryRetainPower;
import eatyourbeets.utilities.GameActions;

public class Enchantment2 extends Enchantment
{
    public static final EYBCardData DATA = RegisterInternal(Enchantment2.class);
    public static final int INDEX = 2;
    public static final int UP5_LOSE_HP = 4;
    public static final int POWER_AMOUNT = 3;

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
            case 1: upgradeMagicNumber(2); break;
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
        return upgradeIndex == 5 ? super.GetRawDescription(POWER_AMOUNT, UP5_LOSE_HP) : super.GetRawDescription(POWER_AMOUNT);
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
            GameActions.Bottom.LoseHP(UP5_LOSE_HP, AttackEffects.NONE).IgnoreTempHP(true);
        }
    }

    @Override
    public void UsePower(AbstractMonster m)
    {
        if (!upgraded)
        {
            for (Affinity t : Affinity.Basic())
            {
                final AbstractAffinityPower p = CombatStats.Affinities.GetPower(t);
                if (p.amountGainedThisTurn > 0)
                {
                    p.RetainOnce();
                }
            }
            return;
        }

        final Affinity affinity = GetAffinity();
        GameActions.Bottom.StackAffinityPower(affinity, POWER_AMOUNT, true);
        switch (affinity)
        {
            case Red:
                GameActions.Bottom.GainPlatedArmor(magicNumber);
                break;

            case Green:
                GameActions.Bottom.StackPower(new TemporaryRetainPower(player, magicNumber));
                break;

            case Blue:
                GameActions.Bottom.GainOrbSlots(magicNumber);
                break;

            case Light:
                GameActions.Bottom.GainArtifact(magicNumber);
                break;

            case Dark:
                GameActions.Bottom.GainTemporaryHP(magicNumber);
                break;
        }
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