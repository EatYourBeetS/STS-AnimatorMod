package pinacolada.actions.cardManipulation;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import eatyourbeets.utilities.Colors;
import pinacolada.actions.utility.GenericCardSelection;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardAffinities;
import pinacolada.cards.base.PCLCardAffinity;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

public class ModifyAffinityScaling extends GenericCardSelection
{
    protected PCLAffinity affinity;
    protected boolean relative;
    protected Color flashColor = Colors.Gold(1).cpy();
    protected int scaling;

    protected ModifyAffinityScaling(AbstractCard card, CardGroup group, int amount, PCLAffinity affinity, int scaling, boolean relative)
    {
        super(card, group, amount);

        this.affinity = affinity;
        this.scaling = scaling;
        this.relative = relative;
    }

    public ModifyAffinityScaling(CardGroup group, int amount, PCLAffinity affinity, int scaling, boolean relative)
    {
        this(null, group, amount, affinity, scaling, relative);
    }

    public ModifyAffinityScaling(AbstractCard card, PCLAffinity affinity, int scaling, boolean relative)
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
        return super.CanSelect(card) && card instanceof PCLCard && ((PCLCard) card).CanScale();
    }

    @Override
    protected void SelectCard(AbstractCard card)
    {
        super.SelectCard(card);

        PCLCard eCard = PCLJUtils.SafeCast(card, PCLCard.class);
        if (eCard == null) {
            return;
        }

        if (flashColor != null)
        {
            PCLGameUtilities.Flash(eCard, flashColor, true);
        }

        final PCLCardAffinities affinities = eCard.affinities;
        if (affinity == null) { // Random scaling
            ChangeScaling(affinities.Get(PCLJUtils.Random(PCLAffinity.Extended()), true));
        }
        else if (affinity == PCLAffinity.General) // Modify all existing scaling
        {
            for (PCLCardAffinity a : affinities.List)
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

    protected void ChangeScaling(PCLCardAffinity affinity)
    {
        affinity.scaling = relative ? (affinity.scaling + scaling) : scaling;
    }
}
