package eatyourbeets.powers.monsters;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import eatyourbeets.actions.special.DieAction;
import eatyourbeets.interfaces.listeners.OnTryApplyPowerListener;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.common.BurningPower;
import eatyourbeets.powers.replacement.PlayerFlightPower;
import eatyourbeets.utilities.GameActions;

public class HornedBat_RedPower extends AnimatorPower implements OnTryApplyPowerListener
{
    public static final String POWER_ID = CreateFullID(HornedBat_RedPower.class);

    public HornedBat_RedPower(AbstractCreature owner)
    {
        super(owner, POWER_ID);

        Initialize(-1);
    }

    @Override
    public boolean TryApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source, AbstractGameAction action)
    {
        return (target != owner || !power.ID.equals(BurningPower.POWER_ID));
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount)
    {
        GameActions.Last.Callback(this::onSpecificTrigger);

        return super.onAttacked(info, damageAmount);
    }

    @Override
    public void onSpecificTrigger()
    {
        super.onSpecificTrigger();

        if (!owner.hasPower(PlayerFlightPower.POWER_ID))
        {
            GameActions.Bottom.Add(new DieAction(owner));
        }
    }
}
