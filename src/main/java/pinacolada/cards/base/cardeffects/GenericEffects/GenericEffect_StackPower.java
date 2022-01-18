package pinacolada.cards.base.cardeffects.GenericEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardTooltip;
import pinacolada.powers.PCLPowerHelper;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLActions;

public class GenericEffect_StackPower extends GenericEffect
{
    protected final PCLPowerHelper power;
    protected final boolean temporary;

    public GenericEffect_StackPower(PCLPowerHelper power, int amount)
    {
        this(power,power.Tooltip,amount,false);
    }

    public GenericEffect_StackPower(PCLPowerHelper power, PCLCardTooltip tooltip, int amount)
    {
        this(power,tooltip,amount,false);
    }

    public GenericEffect_StackPower(PCLPowerHelper power, PCLCardTooltip tooltip, int amount, boolean temporary)
    {
        this.power = power;
        this.tooltip = tooltip;
        this.amount = amount;
        this.temporary = temporary;
    }

    @Override
    public String GetText()
    {
        return temporary ? GR.PCL.Strings.Actions.GainTemporaryAmount(amount, tooltip, true) : GR.PCL.Strings.Actions.GainAmount(amount, tooltip, true);
    }

    @Override
    public void Use(PCLCard card, AbstractPlayer p, AbstractMonster m)
    {
        PCLActions.Bottom.StackPower(TargetHelper.Player(), power, amount);
    }
}
