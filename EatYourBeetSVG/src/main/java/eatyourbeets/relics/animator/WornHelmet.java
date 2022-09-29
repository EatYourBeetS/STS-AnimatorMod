package eatyourbeets.relics.animator;

import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.interfaces.subscribers.OnAffinitySealedSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class WornHelmet extends AnimatorRelic implements OnAffinitySealedSubscriber
{
    public static final String ID = CreateFullID(WornHelmet.class);
    public static final int BLOCK_AMOUNT = 5;

    public WornHelmet()
    {
        super(ID, RelicTier.COMMON, LandingSound.HEAVY);
    }

    @Override
    public String getUpdatedDescription()
    {
        return FormatDescription(0, BLOCK_AMOUNT);
    }

    @Override
    protected void RefreshBattleEffect(boolean enabled)
    {
        super.RefreshBattleEffect(enabled);

        CombatStats.onAffinitySealed.ToggleSubscription(this, enabled);
    }

    @Override
    public void OnAffinitySealed(EYBCard card, boolean manual)
    {
        if (GameUtilities.IsHindrance(card))
        {
            GameActions.Bottom.GainBlock(BLOCK_AMOUNT);
            flash();
        }
    }
}