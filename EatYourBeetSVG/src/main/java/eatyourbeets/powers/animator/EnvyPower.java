package eatyourbeets.powers.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.AnimatorPower;

public class EnvyPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(EnvyPower.class.getSimpleName());

    public EnvyPower(AbstractPlayer owner, int amount)
    {
        super(owner, POWER_ID);

        this.amount = amount;

        updateDescription();
    }

    @Override
    public void atStartOfTurn()
    {
        super.atStartOfTurn();

        Synergies.AddPreemptiveSynergies(amount);
    }
}