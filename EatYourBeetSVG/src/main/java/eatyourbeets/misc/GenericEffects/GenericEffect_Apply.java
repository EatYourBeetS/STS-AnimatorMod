package eatyourbeets.misc.GenericEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardTooltip;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.TargetHelper;

public class GenericEffect_Apply extends GenericEffect
{
    protected final TargetHelper target;
    protected final PowerHelper power;

    public GenericEffect_Apply(TargetHelper target, PowerHelper power, int amount)
    {
        this(target, power, power.Tooltip, amount);
    }

    public GenericEffect_Apply(TargetHelper target, PowerHelper power, EYBCardTooltip tooltip, int amount)
    {
        this.target = target;
        this.power = power;
        this.tooltip = tooltip;
        this.amount = amount;
    }

    @Override
    public String GetText()
    {
        return GR.Animator.Strings.Actions.GainAmount(amount, "["+tooltip.title+"]", true);
    }

    @Override
    public void Use(AnimatorCard card, AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.StackPower(TargetHelper.Player(), power, amount);
    }
}
