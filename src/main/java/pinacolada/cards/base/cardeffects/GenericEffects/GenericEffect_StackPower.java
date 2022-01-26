package pinacolada.cards.base.cardeffects.GenericEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.powers.PCLPowerHelper;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLActions;

public class GenericEffect_StackPower extends GenericEffect
{
    public static final String ID = Register(GenericEffect_StackPower.class);

    protected final PCLPowerHelper power;

    public GenericEffect_StackPower(PCLPowerHelper power, int amount)
    {
        super(ID, power.ID, power.Tooltip, PCLCardTarget.Self, amount);
        this.power = power;
    }

    @Override
    public String GetText()
    {
        return power.EndTurnBehavior == PCLPowerHelper.Behavior.Temporary ? GR.PCL.Strings.Actions.GainTemporaryAmount(amount, tooltip, true) : GR.PCL.Strings.Actions.GainAmount(amount, tooltip, true);
    }

    @Override
    public void Use(PCLCard card, AbstractPlayer p, AbstractMonster m)
    {
        PCLActions.Bottom.StackPower(target.GetTarget(m), power, amount);
    }
}
