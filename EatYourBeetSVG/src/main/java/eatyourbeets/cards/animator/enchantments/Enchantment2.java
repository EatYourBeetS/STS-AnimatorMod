package eatyourbeets.cards.animator.enchantments;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.tokens.AffinityToken;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.SFX;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.PowerTriggerCondition;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Enchantment2 extends Enchantment
{
    public static final EYBCardData DATA = RegisterInternal(Enchantment2.class);
    public static final int INDEX = 2;

    public Enchantment2()
    {
        super(DATA, INDEX);

        Initialize(0, 0, 2, 1);
    }

    @Override
    protected void OnUpgrade()
    {
        upgradeSecondaryValue(upgradeIndex);
    }

    @Override
    public int GetMaxUpgradeIndex()
    {
        return 3;
    }

    @Override
    public boolean CanUsePower(int cost)
    {
        return upgradeIndex > 0 || super.CanUsePower(cost);
    }

    @Override
    public void SetUses(PowerTriggerCondition triggerCondition)
    {
        triggerCondition.SetUses(1, false, true);
    }

    @Override
    public void PayPowerCost(int cost)
    {
        if (upgradeIndex >= 1)
        {
            GameActions.Bottom.TakeDamageAtEndOfTurn(secondaryValue);
        }
        else
        {
            super.PayPowerCost(cost);
        }
    }

    @Override
    public void UsePower(AbstractMonster m)
    {
        if (!upgraded || upgradeIndex == 1)
        {
            GameActions.Bottom.Add(AffinityToken.SelectTokenAction(name, upgraded, false, 1)
            .AddCallback(cards ->
            {
                for (AbstractCard c : cards)
                {
                    GameActions.Bottom.MakeCardInHand(c);
                }
            }));
        }
        else if (upgradeIndex == 2)
        {
            for (Affinity a : Affinity.Basic())
            {
                CombatStats.Affinities.AddTempAffinity(a, magicNumber);
            }
            GameActions.Bottom.SFX(SFX.RELIC_ACTIVATION, 0.95f, 1.05f);
        }
        else if (upgradeIndex == 3)
        {
            GameActions.Last.SelectFromHand(name, 1, false)
            .SetFilter(GameUtilities::CanSeal)
            .AddCallback(cards ->
            {
                for (AbstractCard c : cards)
                {
                    GameActions.Bottom.SealAffinities(c, false);
                }
            });
        }
        else
        {
            throw new RuntimeException("Invalid upgrade index: " + upgradeIndex);
        }
    }
}