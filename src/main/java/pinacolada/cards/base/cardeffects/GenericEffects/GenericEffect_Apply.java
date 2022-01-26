package pinacolada.cards.base.cardeffects.GenericEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.powers.PCLPowerHelper;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLActions;

public class GenericEffect_Apply extends GenericEffect
{
    public static final String ID = Register(GenericEffect_Apply.class);

    protected final PCLPowerHelper power;

    public GenericEffect_Apply(PCLPowerHelper power, int amount)
    {
        super(ID, power.ID, power.Tooltip, PCLCardTarget.Normal, amount);
        this.power = power;
    }

    @Override
    public String GetText()
    {
        return GR.PCL.Strings.Actions.Apply(amount, tooltip.GetTitleOrIcon(), true);
    }

    @Override
    public void Use(PCLCard card, AbstractPlayer p, AbstractMonster m)
    {
        PCLActions.Bottom.StackPower(target.GetTarget(m), power, amount);
    }
}
