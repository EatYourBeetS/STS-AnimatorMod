package eatyourbeets.powers.UnnamedReign;

import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.powers.animator.AnimatorPower;
import eatyourbeets.powers.PlayerStatistics;

public abstract class AbstractCrystalPower extends AnimatorPower
{
    public AbstractCrystalPower(String id, AbstractCreature owner, int value)
    {
        super(owner, id);

        this.amount = value;

        updateDescription();
    }

    @Override
    public void updateDescription()
    {
        String[] desc = powerStrings.DESCRIPTIONS;

        description = desc[0] + amount + desc[1];
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action)
    {
        super.onUseCard(card, action);

        if (action.target == owner)
        {
            for (AbstractCreature c : PlayerStatistics.GetAllCharacters(true))
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
