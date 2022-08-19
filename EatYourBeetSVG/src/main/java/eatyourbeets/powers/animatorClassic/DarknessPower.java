package eatyourbeets.powers.animatorClassic;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import eatyourbeets.cards.animatorClassic.special.Darkness_Adrenaline;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;

public class DarknessPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(DarknessPower.class);

    public DarknessPower(AbstractPlayer owner, int amount)
    {
        super(owner, POWER_ID);

        this.amount = amount;

        updateDescription();
    }

    @Override
    public void wasHPLost(DamageInfo info, int damageAmount)
    {
        super.wasHPLost(info, damageAmount);

        if (info.type != DamageInfo.DamageType.HP_LOSS && damageAmount > 0)
        {
            flash();
            GameActions.Top.ReducePower(this, 1);
            GameActions.Bottom.MakeCardInDrawPile(new Darkness_Adrenaline());
        }
    }
}
