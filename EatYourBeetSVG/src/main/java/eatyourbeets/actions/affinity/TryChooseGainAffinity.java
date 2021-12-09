package eatyourbeets.actions.affinity;

import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.AffinityChoice;
import eatyourbeets.cards.base.AffinityChoiceBuilder;
import eatyourbeets.misc.GenericEffects.GenericEffect_GainAffinity;

public class TryChooseGainAffinity extends TryChooseAffinity
{
    public TryChooseGainAffinity(String sourceName, int gain)
    {
        this(ActionType.CARD_MANIPULATION, sourceName, 1, gain, Affinity.Extended());
    }

    public TryChooseGainAffinity(String sourceName, int gain, Affinity... affinities)
    {
        this(ActionType.CARD_MANIPULATION, sourceName, 1, gain, affinities);
    }

    public TryChooseGainAffinity(String sourceName, int amount, int gain)
    {
        this(ActionType.CARD_MANIPULATION, sourceName, amount, gain, Affinity.Extended());
    }

    public TryChooseGainAffinity(ActionType type, String sourceName, int amount, int gain, Affinity... affinities)
    {
        super(type, sourceName, amount, gain, affinities);
    }

    @Override
    protected AffinityChoice GetCard(Affinity affinity) {
        GenericEffect_GainAffinity affinityGain = new GenericEffect_GainAffinity(affinity, cost);
        AffinityChoiceBuilder builder = new AffinityChoiceBuilder(affinity, cost, affinityGain.GetText()).SetOnUse(affinityGain::Use);
        return builder.Build();
    }
}
