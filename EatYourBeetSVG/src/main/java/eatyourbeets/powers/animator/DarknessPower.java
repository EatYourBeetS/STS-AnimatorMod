package eatyourbeets.powers.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import eatyourbeets.cards.animator.special.DarknessAdrenaline;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;

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
        GameActions.Top.ReducePower(this, 1);
        GameActions.Bottom.MakeCardInDrawPile(new DarknessAdrenaline());

        return super.onLoseHp(damageAmount);
    }
}
