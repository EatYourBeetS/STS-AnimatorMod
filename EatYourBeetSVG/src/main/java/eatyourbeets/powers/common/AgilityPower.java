package eatyourbeets.powers.common;

import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.utilities.GameActions;

public class AgilityPower extends PlayerAttributePower
{
    public static final String POWER_ID = CreateFullID(AgilityPower.class);

    public static int GetCurrentLevel()
    {
        return GetLevel(AgilityPower.class);
    }

    public static void PreserveOnce()
    {
        preservedPowers.Subscribe(POWER_ID);
    }

    public static void StartAlwaysPreserve()
    {
        permanentlyPreservedPowers.Subscribe(POWER_ID);
    }

    public static void StopAlwaysPreserve()
    {
        permanentlyPreservedPowers.Subscribe(POWER_ID);
    }

    public static void StartDisable()
    {
        permanentlyDisabledPowers.Subscribe(POWER_ID);
    }

    public static void StopDisable()
    {
        permanentlyDisabledPowers.Unsubscribe(POWER_ID);
    }


    public AgilityPower(AbstractCreature owner, int amount)
    {
        super(POWER_ID, owner, amount);
    }

    @Override
    public float GetScaling(EYBCard card)
    {
        return card.agilityScaling * amount;
    }

    @Override
    protected void OnThresholdReached()
    {
        GameActions.Top.GainDexterity(1);
    }
}