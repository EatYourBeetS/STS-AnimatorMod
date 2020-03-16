package eatyourbeets.powers.UnnamedReign;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameUtilities;

public class DarkCubePower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(DarkCubePower.class.getSimpleName());

    public DarkCubePower(AbstractCreature owner, int value)
    {
        super(owner, POWER_ID);

        this.amount = value;
        this.priority = -99;

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

        if (owner.isPlayer)
        {
            for (AbstractCreature m : GameUtilities.GetAllEnemies(true))
            {
                GameActions.Bottom.ApplyConstricted(owner, m, amount);
            }
        }
        else
        {
            GameActions.Bottom.ApplyConstricted(null, AbstractDungeon.player, amount);
        }

        this.flash();
    }
}
