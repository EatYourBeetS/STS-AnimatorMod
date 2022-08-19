package eatyourbeets.actions.cardManipulation;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import eatyourbeets.actions.utility.GenericCardSelection;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.cards.base.EYBCardAffinities;
import eatyourbeets.cards.base.EYBCardAffinity;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.Colors;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.Mathf;

public class ModifyAffinityLevel extends GenericCardSelection
{
    protected Affinity affinity;
    protected boolean relative;
    protected boolean includeStar;
    protected boolean seal;
    protected boolean seal_reshuffle;
    protected boolean seal_free;

    protected Color flashColor = Colors.Gold(1).cpy();
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

    public ModifyAffinityLevel Seal(boolean seal, boolean reshuffle)
    {
        this.seal = seal;
        this.seal_reshuffle = reshuffle;

        return this;
    }

    public ModifyAffinityLevel Flash(Color flashColor)
    {
        this.flashColor = flashColor.cpy();

        return this;
    }

    public ModifyAffinityLevel IncludeStar(boolean includeStar)
    {
        this.includeStar = includeStar;

        return this;
    }

    @Override
    protected boolean CanSelect(AbstractCard card)
    {
        final EYBCardAffinities a = GameUtilities.GetAffinities(card);
        return a != null && (!seal || GameUtilities.CanSeal(card) || forceSelect);
    }

    @Override
    protected void SelectCard(AbstractCard card)
    {
        super.SelectCard(card);

        boolean changed = false;
        final EYBCardAffinities affinities = ((EYBCard) card).affinities;
        if (seal)
        {
            if (changed = !affinities.sealed)
            {
                CombatStats.Affinities.Seal(affinities, seal_reshuffle);
            }
        }
        else if (affinity == Affinity.General) // Modify all existing levels
        {
            for (EYBCardAffinity a : affinities.List)
            {
                if (a.level > 0)
                {
                    changed |= ChangeLevel(a);
                }
            }

            if (affinities.Star != null && affinities.Star.level > 0)
            {
                changed |= ChangeLevel(affinities.Star);
            }
        }
        else if (affinity == Affinity.Star || affinities.GetLevel(Affinity.Star) <= 0)
        {
            changed = ChangeLevel(affinities.Get(affinity, true));
        }

        if (changed)
        {
            if (flashColor != null)
            {
                GameUtilities.Flash(card, flashColor, true);
            }
        }
        else
        {
            result.remove(card);
        }

        affinities.Refresh();
    }

    protected boolean ChangeLevel(EYBCardAffinity affinity)
    {
        final int newLevel = Mathf.Clamp(relative ? (affinity.level + level) : level, 0, 2);
        if (affinity.level != newLevel)
        {
            affinity.level = newLevel;
            return true;
        }

        return false;
    }
}
