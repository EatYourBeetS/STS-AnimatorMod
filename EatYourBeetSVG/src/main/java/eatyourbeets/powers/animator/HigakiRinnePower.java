package eatyourbeets.powers.animator;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import eatyourbeets.actions.animator.HigakiRinneAction;
import eatyourbeets.cards.animator.series.Katanagatari.HigakiRinne;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;

public class HigakiRinnePower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(HigakiRinnePower.class);

    private final HigakiRinne higakiRinne;

    public HigakiRinnePower(AbstractCreature owner, HigakiRinne higakiRinne, int amount)
    {
        super(owner, POWER_ID);

        this.higakiRinne = higakiRinne;

        Initialize(amount);
    }

    @Override
    public void atStartOfTurn()
    {
        super.atStartOfTurn();

        GameActions.Bottom.Add(new HigakiRinneAction(higakiRinne, amount));
        this.flash();
    }

    @Override
    public AbstractPower makeCopy()
    {
        return new HigakiRinnePower(owner, higakiRinne, amount);
    }
}
