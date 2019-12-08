package eatyourbeets.powers.UnnamedReign;

import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.StrengthPower;
import eatyourbeets.utilities.GameActionsHelper; import eatyourbeets.utilities.GameActionsHelper2;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.animator.BurningPower;
import eatyourbeets.utilities.GameUtilities;

public class FireCrystalPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(FireCrystalPower.class.getSimpleName());

    public FireCrystalPower(AbstractCreature owner, int value)
    {
        super(owner, POWER_ID);

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
            for (AbstractCreature c : GameUtilities.GetAllCharacters(true))
            {
                if (c != owner)
                {
                    GameActionsHelper.ApplyPowerSilently(null, c, new BurningPower(c, null, amount), amount);
                    GameActionsHelper.ApplyPower(null, c, new StrengthPower(c, amount), amount);
                }
            }

            this.flash();
        }
    }
}
