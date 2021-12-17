package pinacolada.powers.deprecated;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import pinacolada.powers.PCLPower;
import pinacolada.utilities.PCLActions;

public class DexterityTrainingPower extends PCLPower
{
    public static final String POWER_ID = CreateFullID(DexterityTrainingPower.class);

    public DexterityTrainingPower(AbstractPlayer owner, int duration)
    {
        super(owner, POWER_ID);

        this.amount = duration;
        this.isTurnBased = true;

        updateDescription();
    }

    @Override
    public void atStartOfTurnPostDraw()
    {
        super.atStartOfTurnPostDraw();

        PCLActions.Bottom.GainDexterity(1);
        PCLActions.Bottom.ReducePower(this, 1);
    }
}
