package pinacolada.powers.common;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import eatyourbeets.interfaces.subscribers.OnChannelOrbSubscriber;
import pinacolada.powers.PCLCombatStats;
import pinacolada.powers.PCLPower;
import pinacolada.utilities.PCLGameUtilities;

public class SorceryPower extends PCLPower implements OnChannelOrbSubscriber
{
    public static final String POWER_ID = CreateFullID(SorceryPower.class);

    public SorceryPower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);

        Initialize(amount);
    }

    @Override
    public void onInitialApplication()
    {
        super.onInitialApplication();

        PCLCombatStats.onChannelOrb.Subscribe(this);
    }

    @Override
    public void onRemove()
    {
        super.onRemove();

        PCLCombatStats.onChannelOrb.Unsubscribe(this);
    }


    @Override
    public void OnChannelOrb(AbstractOrb orb) {
        PCLGameUtilities.ModifyOrbBaseFocus(orb, amount, true, false);
        RemovePower();
    }
}
