package eatyourbeets.powers.monsters;

import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameUtilities;

public abstract class AbstractCrystalPower extends AnimatorPower
{
    public AbstractCrystalPower(String id, AbstractCreature owner, int amount)
    {
        super(owner, id);

        Initialize(amount);
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action)
    {
        super.onUseCard(card, action);

        if (action.target == owner)
        {
            for (AbstractCreature c : GameUtilities.GetAllCharacters(true))
            {
                if (c != owner)
                {
                    Activate(c);
                }
            }

            this.flash();
        }
    }

    protected abstract void Activate(AbstractCreature target);
}
