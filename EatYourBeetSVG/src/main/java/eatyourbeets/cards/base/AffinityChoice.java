package eatyourbeets.cards.base;

public class AffinityChoice extends AnimatorCard_Dynamic
{
    public final Affinity Affinity;

    public AffinityChoice(AffinityChoiceBuilder builder)
    {
        super(builder);
        this.Affinity = builder.affinity;
    }
}