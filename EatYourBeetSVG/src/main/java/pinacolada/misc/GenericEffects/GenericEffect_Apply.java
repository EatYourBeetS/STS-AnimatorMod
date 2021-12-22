package pinacolada.misc.GenericEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardTooltip;
import pinacolada.powers.PCLPowerHelper;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLActions;

public class GenericEffect_Apply extends GenericEffect
{
    protected final TargetHelper target;
    protected final PCLPowerHelper power;

    public GenericEffect_Apply(TargetHelper target, PCLPowerHelper power, int amount)
    {
        this(target, power, power.Tooltip, amount);
    }

    public GenericEffect_Apply(TargetHelper target, PCLPowerHelper power, PCLCardTooltip tooltip, int amount)
    {
        this.target = target;
        this.power = power;
        this.tooltip = tooltip;
        this.amount = amount;
    }

    @Override
    public String GetText()
    {
        return GR.PCL.Strings.Actions.Apply(amount, "["+tooltip.title+"]", true);
    }

    @Override
    public void Use(PCLCard card, AbstractPlayer p, AbstractMonster m)
    {
        PCLActions.Bottom.StackPower(m, power.Create(m, p, amount));
    }
}
