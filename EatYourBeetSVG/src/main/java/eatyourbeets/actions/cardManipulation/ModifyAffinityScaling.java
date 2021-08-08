package eatyourbeets.actions.cardManipulation;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import eatyourbeets.actions.utility.GenericCardSelection;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.cards.base.EYBCardAffinities;
import eatyourbeets.cards.base.EYBCardAffinity;
import eatyourbeets.utilities.GameUtilities;

public class ModifyAffinityScaling extends GenericCardSelection
{
    protected Affinity affinity;
    protected boolean relative;
    protected boolean flash = true;
    protected int scaling;

    protected ModifyAffinityScaling(AbstractCard card, CardGroup group, int amount, Affinity affinity, int scaling, boolean relative)
    {
        super(card, group, amount);

        this.affinity = affinity;
        this.scaling = scaling;
        this.relative = relative;
    }

    public ModifyAffinityScaling(CardGroup group, int amount, Affinity affinity, int scaling, boolean relative)
    {
        this(null, group, amount, affinity, scaling, relative);
    }

    public ModifyAffinityScaling(AbstractCard card, Affinity affinity, int scaling, boolean relative)
    {
        this(card, null, 1, affinity, scaling, relative);
    }

    public ModifyAffinityScaling Flash(boolean flash)
    {
        this.flash = flash;

        return this;
    }

    @Override
    protected boolean CanSelect(AbstractCard card)
    {
        return super.CanSelect(card) && card instanceof EYBCard && (card.baseBlock > 0 || card.baseDamage > 0);
    }

    @Override
    protected void SelectCard(AbstractCard card)
    {
        super.SelectCard(card);

        if (flash)
        {
            GameUtilities.Flash(card, false);
        }

        final EYBCardAffinities affinities = ((EYBCard) card).affinities;
        if (affinity == Affinity.General) // Modify all existing scaling
        {
            for (EYBCardAffinity a : affinities.List)
            {
                if (a.scaling > 0)
                {
                    ChangeScaling(a);
                }
            }

            if (affinities.Star != null && affinities.Star.scaling > 0)
            {
                ChangeScaling(affinities.Star);
            }
        }
        else
        {
            ChangeScaling(affinities.Get(affinity, true));
        }
    }

    protected void ChangeScaling(EYBCardAffinity affinity)
    {
        affinity.scaling = relative ? (affinity.scaling + scaling) : scaling;
    }
}
