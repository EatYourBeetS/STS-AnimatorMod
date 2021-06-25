package eatyourbeets.powers.monsters;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.PoisonPower;
import eatyourbeets.actions.special.DieAction;
import eatyourbeets.interfaces.listeners.OnTryApplyPowerListener;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.common.PlayerFlightPower;
import eatyourbeets.utilities.GameActions;

public class HornedBat_PurplePower extends AnimatorPower implements OnTryApplyPowerListener
{
    public static final String POWER_ID = CreateFullID(HornedBat_PurplePower.class);

    public HornedBat_PurplePower(AbstractCreature owner)
    {
        super(owner, POWER_ID);

        this.amount = -1;

        updateDescription();
    }

    @Override
    public boolean TryApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source)
    {
        return (target != owner || !power.ID.equals(PoisonPower.POWER_ID));
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
