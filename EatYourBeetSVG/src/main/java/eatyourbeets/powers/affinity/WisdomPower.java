package eatyourbeets.powers.affinity;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.interfaces.subscribers.OnChannelOrbSubscriber;
import eatyourbeets.utilities.GameUtilities;

public class WisdomPower extends AbstractAffinityPower implements OnChannelOrbSubscriber
{
    public static final String POWER_ID = CreateFullID(WisdomPower.class);
    public static final Affinity AFFINITY_TYPE = Affinity.Blue;

    public WisdomPower()
    {
        super(AFFINITY_TYPE, POWER_ID);
    }

    @Override
    public void OnChannelOrb(AbstractOrb orb) {
        final AbstractCard last = GameUtilities.GetLastCardPlayed(true, 1);
        float multiplier = GetChargeMultiplier();
        if (TryUse(last)) {
            GameUtilities.ModifyOrbBaseEvokeAmount(orb, GameUtilities.GetOrbBaseEvokeAmount(orb), false, false);
            GameUtilities.ModifyOrbBasePassiveAmount(orb, GameUtilities.GetOrbBasePassiveAmount(orb), false, false);
        }
    }
}