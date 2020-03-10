package eatyourbeets.powers.animator;

import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.beta.MatouShinji_CommandSpell;
import eatyourbeets.interfaces.markers.Spellcaster;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;

public class CommandSpellPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(CommandSpellPower.class.getSimpleName());

    public CommandSpellPower(AbstractCreature owner, int amount)
    {
        super(owner, MatouShinji_CommandSpell.DATA);
        this.amount = amount;
        updateDescription();
    }

    @Override
    public void updateDescription()
    {
        this.description = FormatDescription(0, amount);
        this.enabled = (amount > 0);
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action)
    {
        if (card.type != AbstractCard.CardType.POWER && card instanceof Spellcaster)
        {
            this.flash();

            AbstractMonster m = null;
            if (action.target != null)
            {
                m = (AbstractMonster) action.target;
            }

            GameActions.Top.PlayCopy(card, m);
            GameActions.Bottom.ReducePower(this, 1);
        }
    }
}

