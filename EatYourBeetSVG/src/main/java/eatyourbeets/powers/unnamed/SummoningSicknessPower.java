package eatyourbeets.powers.unnamed;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.interfaces.subscribers.OnPlayerMinionActionSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.UnnamedPower;
import eatyourbeets.utilities.GameActions;

public class SummoningSicknessPower extends UnnamedPower implements OnPlayerMinionActionSubscriber
{
    public static final String POWER_ID = CreateFullID(SummoningSicknessPower.class);

    public SummoningSicknessPower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);

        Initialize(amount, PowerType.DEBUFF, true);
    }

    @Override
    public void onInitialApplication()
    {
        super.onInitialApplication();

        CombatStats.onPlayerMinionAction.Subscribe(this);
    }

    @Override
    public void onRemove()
    {
        super.onRemove();

        CombatStats.onPlayerMinionAction.Unsubscribe(this);
    }

    @Override
    public boolean CanSummonMinion(boolean triggerEvents)
    {
        if (triggerEvents)
        {
            flashWithoutSound();
        }

        return false;
    }

    @Override
    public void onAfterCardPlayed(AbstractCard usedCard)
    {
        super.onAfterCardPlayed(usedCard);

        ReducePower(GameActions.Delayed, 1);
    }
}
