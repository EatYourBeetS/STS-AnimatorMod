package eatyourbeets.powers.monsters;

import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.StrengthPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.common.BurningPower;
import eatyourbeets.utilities.GameUtilities;

public class FireCrystalPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(FireCrystalPower.class);

    public FireCrystalPower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);

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
                    GameActions.Bottom.StackPower(null, new StrengthPower(c, amount));
                    GameActions.Bottom.StackPower(null, new BurningPower(c, null, amount))
                    .ShowEffect(false, true)
                    .IgnoreArtifact(true);
                }
            }

            this.flash();
        }
    }
}