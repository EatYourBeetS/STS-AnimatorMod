package eatyourbeets.powers.animator;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.actions.animator.SupportDamageAction;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;

public class SupportDamagePower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(SupportDamagePower.class);

    public SupportDamagePower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);

        Initialize(amount);
    }

    @Override
    public void atEndOfTurn(boolean isPlayer)
    {
        super.atEndOfTurn(isPlayer);

        GameActions.Bottom.Add(new SupportDamageAction(new DamageInfo(owner, amount, DamageInfo.DamageType.NORMAL)));
    }
}