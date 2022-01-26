package pinacolada.cards.base.cardeffects.GenericEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.powers.PCLPowerHelper;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLActions;

public class GenericEffect_ApplyToEnemies extends GenericEffect
{
    public static final String ID = Register(GenericEffect_Apply.class);

    protected final PCLPowerHelper power;

    public GenericEffect_ApplyToEnemies(PCLPowerHelper power, int amount)
    {
        super(ID, power.ID, power.Tooltip, PCLCardTarget.AoE, amount);
        this.power = power;
    }

    @Override
    public String GetText()
    {
        return GR.PCL.Strings.Actions.ApplyToALL(amount, tooltip.GetTitleOrIcon(), true);
    }

    @Override
    public void Use(PCLCard card, AbstractPlayer p, AbstractMonster m)
    {
        PCLActions.Bottom.StackPower(target.GetTarget(m), power, amount);
    }
}
