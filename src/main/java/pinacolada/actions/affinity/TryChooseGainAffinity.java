package pinacolada.actions.affinity;

import pinacolada.cards.base.AffinityChoice;
import pinacolada.cards.base.AffinityChoiceBuilder;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.cardeffects.GenericEffects.GenericEffect_GainAffinity;

public class TryChooseGainAffinity extends TryChooseAffinity
{
    public TryChooseGainAffinity(String sourceName, int gain)
    {
        this(ActionType.CARD_MANIPULATION, sourceName, 1, gain, PCLAffinity.Extended());
    }

    public TryChooseGainAffinity(String sourceName, int gain, PCLAffinity... affinities)
    {
        this(ActionType.CARD_MANIPULATION, sourceName, 1, gain, affinities);
    }

    public TryChooseGainAffinity(String sourceName, int amount, int gain)
    {
        this(ActionType.CARD_MANIPULATION, sourceName, amount, gain, PCLAffinity.Extended());
    }

    public TryChooseGainAffinity(ActionType type, String sourceName, int amount, int gain, PCLAffinity... affinities)
    {
        super(type, sourceName, amount, gain, affinities);
    }

    @Override
    protected AffinityChoice GetCard(PCLAffinity affinity) {
        GenericEffect_GainAffinity affinityGain = new GenericEffect_GainAffinity(cost, affinity);
        AffinityChoiceBuilder builder = (AffinityChoiceBuilder) new AffinityChoiceBuilder(affinity, cost)
                .SetText(affinity.GetTooltip().title, affinityGain.GetText(), null)
                .ShowTypeText(false).SetOnUse(affinityGain::Use);
        return builder.Build();
    }
}
