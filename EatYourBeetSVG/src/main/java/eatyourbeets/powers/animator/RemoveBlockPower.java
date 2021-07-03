package eatyourbeets.powers.animator;

import com.megacrit.cardcrawl.actions.common.RemoveAllBlockAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;

import java.text.DecimalFormat;

public class RemoveBlockPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(RemoveBlockPower.class);

    private float percentage;

    public RemoveBlockPower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);

        this.amount = amount;
        if (this.amount >= 9999)
        {
            this.amount = 9999;
        }
        this.type = PowerType.DEBUFF;
        this.isTurnBased = true;

        updateDescription();
    }

    @Override
    public void atEndOfTurn(boolean isPlayer)
    {
        GameActions.Bottom.Add(new RemoveAllBlockAction(this.owner, this.owner));
    }

    @Override
    public void atEndOfRound()
    {
        super.atEndOfRound();
        GameActions.Bottom.ReducePower(this, 1);
    }

}
