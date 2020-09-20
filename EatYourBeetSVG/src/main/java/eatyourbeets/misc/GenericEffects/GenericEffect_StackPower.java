package eatyourbeets.misc.GenericEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardTooltip;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.TargetHelper;

public class GenericEffect_StackPower extends GenericEffect
{
    protected final PowerHelper power;

    public GenericEffect_StackPower(PowerHelper power, int amount)
    {
        this.power = power;
        this.tooltip = power.Tooltip;
        this.amount = amount;
    }

    public GenericEffect_StackPower(PowerHelper power, EYBCardTooltip tooltip, int amount)
    {
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
