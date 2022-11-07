package eatyourbeets.actions.affinities;

import com.megacrit.cardcrawl.core.Settings;
import eatyourbeets.actions.EYBAction;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;

public class GainAffinity extends EYBAction
{
    private Affinity affinity;
    private boolean temporary;
    private boolean boost;

    public GainAffinity(int amount, boolean temporary, boolean boost)
    {
        super(ActionType.SPECIAL, Settings.ACTION_DUR_XFAST);

        this.temporary = temporary;
        this.boost = boost;

        Initialize(amount);
    }

    @Override
    protected void FirstUpdate()
    {
        if (affinity == Affinity.Sealed)
        {
            CombatStats.Affinities.AddAffinitySealUses(amount);
            return;
        }

        if (temporary)
        {
            CombatStats.Affinities.AddTempAffinity(affinity, amount);
        }
        else
        {
            if (affinity == Affinity.Star)
            {
                for (Affinity a : Affinity.Basic())
                {
                    GameActions.Top.GainAffinity(a, amount, boost);
                }
            }
            else
            {
                GameActions.Top.GainAffinity(affinity, amount, boost);
            }

            Complete();
        }
    }
}

