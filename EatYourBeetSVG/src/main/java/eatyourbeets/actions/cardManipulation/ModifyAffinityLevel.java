package eatyourbeets.actions.cardManipulation;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import eatyourbeets.actions.utility.GenericCardSelection;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.cards.base.EYBCardAffinities;
import eatyourbeets.cards.base.EYBCardAffinity;
import eatyourbeets.utilities.GameUtilities;

public class ModifyAffinityLevel extends GenericCardSelection
{
    protected Affinity affinity;
    protected boolean relative;
    protected boolean flash = true;
    protected int level;

    protected ModifyAffinityLevel(AbstractCard card, CardGroup group, int amount, Affinity affinity, int level, boolean relative)
    {
        super(card, group, amount);

        this.affinity = affinity;
        this.level = level;
        this.relative = relative;
    }

    public ModifyAffinityLevel(CardGroup group, int amount, Affinity affinity, int level, boolean relative)
    {
        this(null, group, amount, affinity, level, relative);
    }

    public ModifyAffinityLevel(AbstractCard card, Affinity affinity, int level, boolean relative)
    {
        this(card, null, 1, affinity, level, relative);
    }

    public ModifyAffinityLevel Flash(boolean flash)
    {
        this.flash = flash;

        return this;
    }

    @Override
    protected boolean CanSelect(AbstractCard card)
    {
        return super.CanSelect(card) && card instanceof EYBCard;
    }

    @Override
    protected void SelectCard(AbstractCard card)
    {
        super.SelectCard(card);

        if (flash)
        {
            GameUtilities.Flash(card, true);
        }

        final EYBCardAffinities affinities = ((EYBCard) card).affinities;
        if (affinity == Affinity.General) // Modify all existing levels
        {
            for (EYBCardAffinity a : affinities.List)
            {
                if (a.level > 0)
                {
                    ChangeLevel(a);
                }
            }

            if (affinities.Star != null && affinities.Star.level > 0)
            {
                ChangeLevel(affinities.Star);
            }
        }
        else
        {
            ChangeLevel(affinities.Get(affinity, true));
        }
    }

    protected void ChangeLevel(EYBCardAffinity affinity)
    {
        affinity.level = relative ? (affinity.level + level) : level;
    }
}
