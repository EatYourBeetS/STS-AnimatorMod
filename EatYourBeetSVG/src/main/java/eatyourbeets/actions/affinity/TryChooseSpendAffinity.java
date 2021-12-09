package eatyourbeets.actions.affinity;

import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.AffinityChoice;
import eatyourbeets.cards.base.AffinityChoiceBuilder;
import eatyourbeets.misc.GenericEffects.GenericEffect_PayAffinity;

public class TryChooseSpendAffinity extends TryChooseAffinity
{
    public TryChooseSpendAffinity(String sourceName, int cost)
    {
        this(ActionType.CARD_MANIPULATION, sourceName, 1, cost, Affinity.Extended());
    }

    public TryChooseSpendAffinity(String sourceName, int cost, Affinity... affinities)
    {
        this(ActionType.CARD_MANIPULATION, sourceName, 1, cost, affinities);
    }

    public TryChooseSpendAffinity(String sourceName, int amount, int cost)
    {
        this(ActionType.CARD_MANIPULATION, sourceName, amount, cost, Affinity.Extended());
    }

    public TryChooseSpendAffinity(ActionType type, String sourceName, int amount, int cost, Affinity... affinities)
    {
        super(type, sourceName, amount, cost, affinities);
    }

    @Override
    protected AffinityChoice GetCard(Affinity affinity) {
        int req = sourceAffinities != null ? sourceAffinities.GetRequirement(sourceAffinities.GetRequirement(Affinity.General) > 0 ? Affinity.General : affinity) : cost >= 0 ? cost : System.GetAffinityLevel(affinity, true);
        if (System.GetAffinityLevel(affinity,true) >= req) {
            GenericEffect_PayAffinity affinityCost = new GenericEffect_PayAffinity(affinity, req);
            AffinityChoiceBuilder builder = new AffinityChoiceBuilder(affinity, cost, affinityCost.GetText()).SetOnUse(affinityCost::Use);
            return builder.Build();
        }
        return null;
    }
}
