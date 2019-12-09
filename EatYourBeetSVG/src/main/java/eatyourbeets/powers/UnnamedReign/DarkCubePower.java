package eatyourbeets.powers.UnnamedReign;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.ConstrictedPower;
import eatyourbeets.utilities.GameActionsHelper_Legacy;
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
            for (AbstractCreature m : GameUtilities.GetCurrentEnemies(true))
            {
                GameActionsHelper_Legacy.ApplyPower(owner, m, new ConstrictedPower(m, owner, amount), amount);
            }
        }
        else
        {
            AbstractPlayer p = AbstractDungeon.player;
            GameActionsHelper_Legacy.ApplyPower(owner, p, new ConstrictedPower(p, owner, amount), amount);
        }
        this.flash();
    }
}
