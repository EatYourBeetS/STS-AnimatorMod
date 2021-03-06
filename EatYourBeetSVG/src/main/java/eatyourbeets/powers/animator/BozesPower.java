package eatyourbeets.powers.animator;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JUtils;

public class BozesPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(BozesPower.class);

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

        RemovePower();
    }

    @Override
    public void onAfterCardPlayed(AbstractCard usedCard)
    {
        super.onAfterCardPlayed(usedCard);

        AnimatorCard card = JUtils.SafeCast(usedCard, AnimatorCard.class);
        if (card != null && card.HasSynergy())
        {
            GameActions.Bottom.StackPower(new SupportDamagePower(owner, amount));

            this.flash();
        }
    }
}
