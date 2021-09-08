package eatyourbeets.powers.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.actions.animator.HigakiRinneAction;
import eatyourbeets.cards.animator.series.Katanagatari.HigakiRinne;

public class HigakiRinnePower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(HigakiRinnePower.class);

    private final HigakiRinne higakiRinne;

    public HigakiRinnePower(AbstractPlayer owner, HigakiRinne higakiRinne, int amount)
    {
        super(owner, POWER_ID);

        this.higakiRinne = higakiRinne;

        Initialize(amount);
    }

    @Override
    public void updateDescription()
    {
        this.description = powerStrings.DESCRIPTIONS[0];
    }

    @Override
    public void atStartOfTurn()
    {
        super.atStartOfTurn();

        GameActions.Bottom.Add(new HigakiRinneAction(higakiRinne, amount));

        this.flash();
    }
}
