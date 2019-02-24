package eatyourbeets.powers;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import eatyourbeets.cards.AnimatorCard;

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
        AnimatorCard.SynergyReserves = amount;
    }

    @Override
    public void atEndOfTurn(boolean isPlayer)
    {
        super.atEndOfTurn(isPlayer);
        AnimatorCard.SynergyReserves = 0;
    }
}
