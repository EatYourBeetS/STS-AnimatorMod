package eatyourbeets.powers.animator;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.ColoredString;
import eatyourbeets.utilities.Colors;
import eatyourbeets.utilities.GameActions;

public class FlamingWeaponPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(FlamingWeaponPower.class);
    public static final int BURNING_AMOUNT = 2;

    public FlamingWeaponPower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);

        Initialize(amount, PowerType.BUFF, true);
    }

    @Override
    public void updateDescription()
    {
        description = FormatDescription(0, BURNING_AMOUNT);
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target)
    {
        super.onAttack(info, damageAmount, target);

        if (damageAmount > 0 && target != this.owner && info.type == DamageInfo.DamageType.NORMAL)
        {
            GameActions.Top.ApplyBurning(owner, target, BURNING_AMOUNT).ShowEffect(false, true);
            this.flash();
        }
    }

    @Override
    public void atEndOfTurn(boolean isPlayer)
    {
        super.atEndOfTurn(isPlayer);

        ReducePower(1);
    }

    @Override
    protected ColoredString GetSecondaryAmount(Color c)
    {
        return new ColoredString(BURNING_AMOUNT, Colors.Green(c.a));
    }
}