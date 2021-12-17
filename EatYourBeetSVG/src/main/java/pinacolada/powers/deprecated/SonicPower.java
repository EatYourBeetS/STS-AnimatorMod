package pinacolada.powers.deprecated;

import com.megacrit.cardcrawl.core.AbstractCreature;
import pinacolada.powers.PCLPower;
import pinacolada.utilities.PCLActions;

public class SonicPower extends PCLPower
{
    public static final String POWER_ID = CreateFullID(SonicPower.class);

    public SonicPower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);

        this.amount = amount;
        this.isTurnBased = true;

        updateDescription();
    }

    @Override
    public void atStartOfTurnPostDraw()
    {
        super.atStartOfTurnPostDraw();

        PCLActions.Bottom.GainDexterity(1);
        PCLActions.Bottom.CreateThrowingKnives(1);

        PCLActions.Bottom.ReducePower(this, 1);
    }
}