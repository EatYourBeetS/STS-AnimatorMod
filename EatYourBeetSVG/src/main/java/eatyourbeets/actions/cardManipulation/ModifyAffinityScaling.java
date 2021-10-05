package eatyourbeets.actions.cardManipulation;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import eatyourbeets.actions.utility.GenericCardSelection;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.cards.base.EYBCardAffinities;
import eatyourbeets.cards.base.EYBCardAffinity;
import eatyourbeets.utilities.Colors;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

public class ModifyAffinityScaling extends GenericCardSelection
{
    protected Affinity affinity;
    protected boolean relative;
    protected Color flashColor = Colors.Gold(1).cpy();
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

    public ModifyAffinityScaling Flash(Color flashColor)
    {
        this.flashColor = flashColor;

        return this;
    }

    @Override
    protected boolean CanSelect(AbstractCard card)
    {
        return super.CanSelect(card) && card instanceof EYBCard && ((EYBCard) card).CanScale();
    }

    @Override
    protected void SelectCard(AbstractCard card)
    {
        super.SelectCard(card);

        EYBCard eCard = JUtils.SafeCast(card, EYBCard.class);
        if (eCard == null) {
            return;
        }

        if (flashColor != null)
        {
            GameUtilities.Flash(eCard, flashColor, true);
        }

        final EYBCardAffinities affinities = eCard.affinities;
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
