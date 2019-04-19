package eatyourbeets.powers;

import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;

public class EnvyPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(EnvyPower.class.getSimpleName());

    private int duration;

    public EnvyPower(AbstractPlayer owner, int amount, boolean upgraded)
    {
        super(owner, POWER_ID);

        if (upgraded)
        {
            duration = -1;
        }
        else
        {
            duration = 4;
        }

        this.amount = amount;

        updateDescription();
    }

    @Override
    public void atStartOfTurn()
    {
        super.atStartOfTurn();
        AnimatorCard.SynergyReserves = amount;

        if (duration > 0)
        {
            duration -= 1;

            if (duration == 0)
            {
                duration = -1;
                AbstractPlayer p = AbstractDungeon.player;
                GameActionsHelper.AddToBottom(new ReducePowerAction(p, p, this, 1));
            }
        }
    }

    @Override
    public void atEndOfTurn(boolean isPlayer)
    {
        super.atEndOfTurn(isPlayer);
        AnimatorCard.SynergyReserves = 0;
    }
}
