package eatyourbeets.relics.animator;

import eatyourbeets.interfaces.subscribers.OnAfterCardDiscardedSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class OddDevice_4 extends OddDevice implements OnAfterCardDiscardedSubscriber
{
    public static final String ID = CreateFullID(OddDevice_4.class);
    public static final int EXHAUST_THRESHOLD = 8;
    public static final int EXHAUST_BLOCK_AMOUNT = 10;
    public static final int SCRY_AMOUNT = 3;
    public static final int DISCARD_INS_AMOUNT = 1;
    public static final int SEAL_AMOUNT = 1;
    public static final int SEAL_MOTIVATE_AMOUNT = 2;

    public OddDevice_4()
    {
        super(ID, RelicTier.BOSS, LandingSound.CLINK);
    }

    @Override
    protected String GetMainDescription(int effectIndex)
    {
        if (effectIndex == 1)
        {
            return FormatDescription(effectIndex, EXHAUST_THRESHOLD, EXHAUST_BLOCK_AMOUNT);
        }
        if (effectIndex == 2)
        {
            return FormatDescription(effectIndex, SCRY_AMOUNT, DISCARD_INS_AMOUNT);
        }
        if (effectIndex == 3)
        {
            return FormatDescription(effectIndex, SEAL_AMOUNT, SEAL_MOTIVATE_AMOUNT);
        }

        throw new RuntimeException("Invalid index: " + effectIndex);
    }

    @Override
    protected void ActivateBattleEffect()
    {
        super.ActivateBattleEffect();

        if (GetEffectIndex() == 3)
        {
            CombatStats.Affinities.AddAffinitySealUses(SEAL_AMOUNT);
            flash();
        }
    }

    @Override
    protected void RefreshBattleEffect(boolean enabled)
    {
        super.RefreshBattleEffect(enabled);

        if (GetEffectIndex() == 2)
        {
            CombatStats.onAfterCardDiscarded.ToggleSubscription(this, enabled);
        }
    }

    @Override
    public void onPlayerEndTurn()
    {
        super.onPlayerEndTurn();

        if (GetEffectIndex() == 1 && player.exhaustPile.size() >= EXHAUST_THRESHOLD)
        {
            GameActions.Bottom.GainBlock(EXHAUST_BLOCK_AMOUNT);
            flash();
        }
    }

    @Override
    public void atTurnStart()
    {
        super.atTurnStart();

        if (GetEffectIndex() == 2)
        {
            GameActions.Top.Scry(SCRY_AMOUNT);
            flash();
        }
    }

    @Override
    public void atTurnStartPostDraw()
    {
        super.atTurnStartPostDraw();

        if (GetEffectIndex() == 3)
        {
            GameActions.Delayed.Motivate(SEAL_MOTIVATE_AMOUNT).SetFilter(GameUtilities::IsSealed);
            flash();
        }
    }

    @Override
    public void OnAfterCardDiscarded()
    {
        if (GetEffectIndex() == 2)
        {
            GameActions.Bottom.GainInspiration(DISCARD_INS_AMOUNT);
            flash();
        }
    }
}