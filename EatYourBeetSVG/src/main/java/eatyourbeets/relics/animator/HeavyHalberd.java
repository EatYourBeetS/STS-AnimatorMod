package eatyourbeets.relics.animator;

import eatyourbeets.cards.base.Affinity;
import eatyourbeets.interfaces.subscribers.OnAffinityThresholdReachedSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.affinity.AbstractAffinityPower;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.TargetHelper;

public class HeavyHalberd extends AnimatorRelic implements OnAffinityThresholdReachedSubscriber
{
    public static final String ID = CreateFullID(HeavyHalberd.class);
    public static final int VULNERABLE_AMOUNT = 2;
    public static final int DRAW_AMOUNT = 2;

    public HeavyHalberd()
    {
        super(ID, RelicTier.RARE, LandingSound.HEAVY);
    }

    @Override
    public String getUpdatedDescription()
    {
        return FormatDescription(0, VULNERABLE_AMOUNT, DRAW_AMOUNT);
    }

    @Override
    protected void RefreshBattleEffect(boolean enabled)
    {
        super.RefreshBattleEffect(enabled);

        CombatStats.onAffinityThresholdReached.ToggleSubscription(this, enabled);
    }

    @Override
    public void OnAffinityThresholdReached(AbstractAffinityPower power, int thresholdLevel)
    {
        if (power.affinity == Affinity.Red)
        {
            GameActions.Bottom.ApplyVulnerable(TargetHelper.Enemies(player), VULNERABLE_AMOUNT);
            flash();
        }
        else if (power.affinity == Affinity.Green)
        {
            GameActions.Bottom.Draw(DRAW_AMOUNT);
            flash();
        }
    }
}