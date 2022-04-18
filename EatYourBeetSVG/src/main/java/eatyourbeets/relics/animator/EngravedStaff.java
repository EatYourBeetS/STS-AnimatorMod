package eatyourbeets.relics.animator;

import eatyourbeets.cards.base.Affinity;
import eatyourbeets.interfaces.subscribers.OnAffinityThresholdReachedSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.affinity.AbstractAffinityPower;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.RandomizedList;

public class EngravedStaff extends AnimatorRelic implements OnAffinityThresholdReachedSubscriber
{
    public static final String ID = CreateFullID(EngravedStaff.class);
    public static final int BOOST_AMOUNT = 1;
    public static final int TEMP_HP_AMOUNT = 1;

    public EngravedStaff()
    {
        super(ID, RelicTier.RARE, LandingSound.MAGICAL);
    }

    @Override
    protected void ActivateBattleEffect()
    {
        super.ActivateBattleEffect();

        CombatStats.onAffinityThresholdReached.Subscribe(this);
    }

    @Override
    protected void DeactivateBattleEffect()
    {
        super.DeactivateBattleEffect();

        CombatStats.onAffinityThresholdReached.Unsubscribe(this);
    }

    @Override
    public void OnAffinityThresholdReached(AbstractAffinityPower power, int thresholdLevel)
    {
        GameActions.Bottom.GainTemporaryHP(TEMP_HP_AMOUNT);
        flash();
    }

    @Override
    public String getUpdatedDescription()
    {
        return FormatDescription(0, TEMP_HP_AMOUNT);
    }

    @Override
    public void atTurnStartPostDraw()
    {
        super.atTurnStartPostDraw();

        GameActions.Bottom.Callback(() ->
        {
            int i = BOOST_AMOUNT;
            final RandomizedList<Affinity> affinities = new RandomizedList<>(Affinity.Basic());
            while (affinities.Size() > 0 && i > 0)
            {
                GameActions.Bottom.StackAffinityPower(affinities.Retrieve(rng), 1, true);
                i -= 1;
            }

            flash();
        });
    }
}