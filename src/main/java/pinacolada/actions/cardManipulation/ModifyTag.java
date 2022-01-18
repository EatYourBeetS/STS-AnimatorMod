package pinacolada.actions.cardManipulation;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import eatyourbeets.utilities.Colors;
import pinacolada.actions.utility.GenericCardSelection;
import pinacolada.cards.base.PCLCard;
import pinacolada.powers.PCLCombatStats;
import pinacolada.utilities.PCLGameUtilities;

public class ModifyTag extends GenericCardSelection
{
    protected AbstractCard.CardTags tag;
    protected boolean value;
    protected Color flashColor = Colors.Gold(1).cpy();

    protected ModifyTag(AbstractCard card, CardGroup group, int amount, AbstractCard.CardTags tag, boolean value)
    {
        super(card, group, amount);

        this.tag = tag;
        this.value = value;
    }

    public ModifyTag(CardGroup group, int amount, AbstractCard.CardTags tag, boolean remove)
    {
        this(null, group, amount, tag, remove);
    }

    public ModifyTag(AbstractCard card, AbstractCard.CardTags tag, boolean remove)
    {
        this(card, null, 1, tag, remove);
    }

    public ModifyTag Flash(Color flashColor)
    {
        this.flashColor = flashColor;

        return this;
    }

    @Override
    protected boolean CanSelect(AbstractCard card)
    {
        return super.CanSelect(card) && card instanceof PCLCard && value ^ card.hasTag(tag);
    }

    @Override
    protected void SelectCard(AbstractCard card)
    {
        super.SelectCard(card);

        if (flashColor != null)
        {
            PCLGameUtilities.Flash(card, flashColor, true);
        }

        PCLGameUtilities.ModifyCardTag(card, tag, value);
        PCLCombatStats.OnTagChanged(card, tag, value);
    }
}
