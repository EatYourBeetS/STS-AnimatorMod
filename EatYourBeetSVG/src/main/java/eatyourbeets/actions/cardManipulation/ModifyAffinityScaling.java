package eatyourbeets.actions.cardManipulation;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import eatyourbeets.actions.utility.GenericCardSelection;
import eatyourbeets.cards.base.AffinityType;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.cards.base.EYBCardAffinity;
import eatyourbeets.utilities.JUtils;

public class ModifyAffinityScaling extends GenericCardSelection
{
    protected AffinityType affinityType;
    protected boolean relative;
    protected int scaling;

    protected ModifyAffinityScaling(AbstractCard card, CardGroup group, int amount, AffinityType affinityType, int scaling, boolean relative)
    {
        super(card, group, amount);

        this.affinityType = affinityType;
        this.scaling = scaling;
        this.relative = relative;
    }

    public ModifyAffinityScaling(CardGroup group, int amount, AffinityType affinityType, int scaling, boolean relative)
    {
        this(null, group, amount, affinityType, scaling, relative);
    }

    public ModifyAffinityScaling(AbstractCard card, AffinityType affinityType, int scaling, boolean relative)
    {
        this(card, null, 1, affinityType, scaling, relative);
    }

    @Override
    protected void SelectCard(AbstractCard card)
    {
        super.SelectCard(card);

        EYBCard c = JUtils.SafeCast(card, EYBCard.class);
        if (c == null)
        {
            return;
        }

        if (affinityType == AffinityType.General)
        {
            for (EYBCardAffinity a : c.affinities.List)
            {
                if (a.scaling > 0)
                {
                    ChangeScaling(a);
                }
            }

            if (c.affinities.Star != null && c.affinities.Star.scaling > 0)
            {
                ChangeScaling(c.affinities.Star);
            }

            return;
        }

        ChangeScaling(c.affinities.Get(affinityType, true));
    }

    protected void ChangeScaling(EYBCardAffinity affinity)
    {
        affinity.scaling = relative ? (affinity.scaling + scaling) : scaling;
    }
}
