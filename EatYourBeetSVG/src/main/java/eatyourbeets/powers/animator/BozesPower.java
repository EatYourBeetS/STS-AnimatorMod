package eatyourbeets.powers.animator;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActionsHelper_Legacy;
import eatyourbeets.utilities.JavaUtilities;
import eatyourbeets.cards.AnimatorCard;

public class BozesPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(BozesPower.class.getSimpleName());

    public BozesPower(AbstractCreature owner, int value)
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
    public void atEndOfTurn(boolean isPlayer)
    {
        super.atEndOfTurn(isPlayer);

        GameActionsHelper_Legacy.AddToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, this));
    }

    @Override
    public void onAfterCardPlayed(AbstractCard usedCard)
    {
        super.onAfterCardPlayed(usedCard);

        AnimatorCard card = JavaUtilities.SafeCast(usedCard, AnimatorCard.class);
        if (card != null && card.HasActiveSynergy())
        {
            if (amount > 0)
            {
                GameActionsHelper_Legacy.ApplyPower(owner, owner, new SupportDamagePower(owner, amount), amount);
                //GameActions.Bottom.GainBlock(owner, amount);

                this.flash();
            }
        }
    }
}
