package eatyourbeets.powers.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.CombatStats;

public class GazelDwargonPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(GazelDwargonPower.class);

    public GazelDwargonPower(AbstractPlayer owner, int amount)
    {
        super(owner, POWER_ID);

        Initialize(amount);
    }

    @Override
    public void atEndOfTurn(boolean isPlayer)
    {
        super.atEndOfTurn(isPlayer);

        if (isPlayer)
        {
            CombatStats.BlockRetained += amount;
        }
    }
}
