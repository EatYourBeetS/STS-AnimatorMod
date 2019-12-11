package eatyourbeets.powers.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.actions.animator.SupportDamageAction;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;

public class SupportDamagePower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(SupportDamagePower.class.getSimpleName());

    public SupportDamagePower(AbstractCreature owner, int stacks)
    {
        super(owner, POWER_ID);

        this.amount = stacks;
        this.type = PowerType.BUFF;

        updateDescription();
    }

    @Override
    public void atEndOfTurn(boolean isPlayer)
    {
        super.atEndOfTurn(isPlayer);

        DamageInfo info = new DamageInfo(owner, amount, DamageInfo.DamageType.NORMAL);
        GameActions.Bottom.Add(new SupportDamageAction(info, AbstractGameAction.AttackEffect.NONE));
    }
}