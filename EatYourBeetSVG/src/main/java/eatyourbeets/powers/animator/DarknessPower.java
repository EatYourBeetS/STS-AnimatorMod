package eatyourbeets.powers.animator;

import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import eatyourbeets.cards.animator.DarknessAdrenaline;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActionsHelper_Legacy;

public class DarknessPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(DarknessPower.class.getSimpleName());

    public DarknessPower(AbstractPlayer owner, int amount)
    {
        super(owner, POWER_ID);

        this.amount = amount;

        updateDescription();
    }

    @Override
    public int onLoseHp(int damageAmount)
    {
        GameActionsHelper_Legacy.SetOrder(GameActionsHelper_Legacy.Order.Top);

        GameActionsHelper_Legacy.AddToDefault(new ReducePowerAction(owner, owner, this, 1));
        GameActionsHelper_Legacy.MakeCardInDrawPile(new DarknessAdrenaline(), 1, false);

        GameActionsHelper_Legacy.ResetOrder();

        return super.onLoseHp(damageAmount);
    }
}
