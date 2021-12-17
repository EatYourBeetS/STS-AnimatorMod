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

public class ModifyAffinityLevel extends GenericCardSelection
{
    protected PCLAffinity affinity;
    protected boolean relative;
    protected Color flashColor = Colors.Gold(1).cpy();
    protected int level;

    protected ModifyAffinityLevel(AbstractCard card, CardGroup group, int amount, PCLAffinity affinity, int level, boolean relative)
    {
        super(card, group, amount);

        this.affinity = affinity;
        this.level = level;
        this.relative = relative;
    }

    public ModifyAffinityLevel(CardGroup group, int amount, PCLAffinity affinity, int level, boolean relative)
    {
        this(null, group, amount, affinity, level, relative);
    }

    public ModifyAffinityLevel(AbstractCard card, PCLAffinity affinity, int level, boolean relative)
    {
        this(card, null, 1, affinity, level, relative);
    }

    public ModifyAffinityLevel Flash(Color flashColor)
    {
        this.flashColor = flashColor;

        return this;
    }

    @Override
    protected boolean CanSelect(AbstractCard card)
    {
        return super.CanSelect(card) && card instanceof PCLCard;
    }

    @Override
    protected void SelectCard(AbstractCard card)
    {
        super.SelectCard(card);

        if (flashColor != null)
        {
            PCLGameUtilities.Flash(card, flashColor, true);
        }

        final PCLCardAffinities affinities = ((PCLCard) card).affinities;
        if (affinity == PCLAffinity.General) // Modify all existing levels
        {
            for (PCLCardAffinity a : affinities.List)
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

    protected void ChangeLevel(PCLCardAffinity affinity)
    {
        affinity.level = relative ? (affinity.level + level) : level;
    }
}
