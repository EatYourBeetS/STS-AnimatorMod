package eatyourbeets.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.actions.AnimatorAction;

public class GuildGirlPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(GuildGirlPower.class.getSimpleName());

    public GuildGirlPower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);
        this.amount = amount;

        updateDescription();
    }

    @Override
    public void atStartOfTurnPostDraw()
    {
        super.atStartOfTurnPostDraw();
        GameActionsHelper.AddToBottom(new GuildGirlAction(this.amount));
    }

    private class GuildGirlAction extends AnimatorAction
    {
        public GuildGirlAction(int amount)
        {
            this.amount = amount;
        }

        @Override
        public void update()
        {
            GameActionsHelper.CycleCardAction(this.amount);

            isDone = true;
        }
    }
}
