package eatyourbeets.powers.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.actions._legacy.animator.KaijinAction;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActionsHelper_Legacy;

public class KaijinPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(KaijinPower.class.getSimpleName());

    public KaijinPower(AbstractPlayer owner, int amount)
    {
        super(owner, POWER_ID);

        this.amount = amount;

        updateDescription();
    }

    @Override
    public void atEndOfTurn(boolean isPlayer)
    {
        super.atEndOfTurn(isPlayer);

        if (isPlayer && !AbstractDungeon.player.hand.isEmpty())
        {
            GameActionsHelper_Legacy.AddToBottom(new KaijinAction(owner, amount));
        }
    }
}
