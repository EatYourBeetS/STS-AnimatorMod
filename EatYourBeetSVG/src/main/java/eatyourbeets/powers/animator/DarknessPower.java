package eatyourbeets.powers.animator;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import eatyourbeets.cards.animator.special.Darkness_Adrenaline;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;

public class DarknessPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(DarknessPower.class);

    public DarknessPower(AbstractPlayer owner, int amount)
    {
        super(owner, POWER_ID);

        Initialize(amount);
    }

    @Override
    public void wasHPLost(DamageInfo info, int damageAmount)
    {
        super.wasHPLost(info, damageAmount);

        if (amount > 0 && info.type != DamageInfo.DamageType.HP_LOSS && damageAmount > 0)
        {
            GameActions.Bottom.MakeCardInDrawPile(new Darkness_Adrenaline());
            if ((amount -= 1) <= 0)
            {
                RemovePower();
            }

            this.flash();
        }
    }
}
