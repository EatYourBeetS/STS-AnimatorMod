package eatyourbeets.powers.animatorClassic;

import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.powers.AnimatorClassicPower;
import eatyourbeets.utilities.GameActions;

public class BorosPower extends AnimatorClassicPower
{
    public static final String POWER_ID = CreateFullID(BorosPower.class);

    public BorosPower(AbstractCreature owner)
    {
        super(owner, POWER_ID);

        this.amount = -1;

        updateDescription();
    }

    @Override
    public void updateDescription()
    {
        String[] desc = powerStrings.DESCRIPTIONS;

        description = desc[0];
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action)
    {
        if ((card.type == AbstractCard.CardType.POWER) && !card.isInAutoplay)
        {
            flash();

            AbstractMonster m = null;
            if (action.target != null)
            {
                m = (AbstractMonster) action.target;
            }

            GameActions.Top.PlayCopy(card, m);
        }
    }
}
