package pinacolada.cards.base;

public class AffinityChoice extends PCLCard_Dynamic
{
    public final PCLAffinity Affinity;

    public AffinityChoice(AffinityChoiceBuilder builder)
    {
        super(builder);
        this.Affinity = builder.affinity;
    }
}