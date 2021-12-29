package pinacolada.actions.cardManipulation;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import eatyourbeets.utilities.Colors;
import pinacolada.actions.utility.GenericCardSelection;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.modifiers.CostModifiers;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

public class ModifyCost extends GenericCardSelection
{
    protected boolean permanent;
    protected boolean relative;
    protected int costChange;
    protected Color flashColor = Colors.Gold(1).cpy();

    protected ModifyCost(AbstractCard card, CardGroup group, int amount, int costChange, boolean permanent, boolean relative)
    {
        super(card, group, amount);

        this.costChange = costChange;
        this.permanent = permanent;
        this.relative = relative;
    }

    public ModifyCost(CardGroup group, int amount, int costChange, boolean permanent, boolean relative)
    {
        this(null, group, amount, costChange, permanent, relative);
    }

    public ModifyCost(AbstractCard card, int costChange, boolean permanent, boolean relative)
    {
        this(card, null, 1, costChange, permanent, relative);
    }

    public ModifyCost Flash(Color flashColor)
    {
        this.flashColor = flashColor;

        return this;
    }

    @Override
    protected boolean CanSelect(AbstractCard card)
    {
        return super.CanSelect(card) && card.costForTurn >= 0;
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

        if (permanent)
        {
            if (relative) {
                CostModifiers.For(card).Add(costChange);
            }
            else {
                CostModifiers.For(card).Set(costChange);
            }
        }
        else
        {
            PCLGameUtilities.ModifyCostForTurn(card, costChange, relative);
        }
    }
}
