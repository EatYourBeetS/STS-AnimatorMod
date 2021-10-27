package eatyourbeets.powers.animator;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.interfaces.subscribers.OnAfterCardPlayedSubscriber;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.common.SupportDamagePower;
import eatyourbeets.utilities.GameActions;

public class BozesPower extends AnimatorPower implements OnAfterCardPlayedSubscriber
{
    public static final String POWER_ID = CreateFullID(BozesPower.class);

    public BozesPower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);

        Initialize(amount);
    }

    @Override
    public void onInitialApplication()
    {
        super.onInitialApplication();

        CombatStats.onAfterCardPlayed.Subscribe(this);
    }

    @Override
    public void onRemove()
    {
        super.onRemove();

        CombatStats.onAfterCardPlayed.Unsubscribe(this);
    }

    @Override
    public void OnAfterCardPlayed(AbstractCard card)
    {
        GameActions.Bottom.StackPower(new SupportDamagePower(owner, amount));
        this.flash();
    }

    @Override
    public void atEndOfTurn(boolean isPlayer)
    {
        super.atEndOfTurn(isPlayer);

        RemovePower();
    }
}
