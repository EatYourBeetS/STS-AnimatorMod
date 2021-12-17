package pinacolada.powers.deprecated;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.LoseDexterityPower;
import pinacolada.powers.PCLPower;
import pinacolada.utilities.PCLActions;

public class NextTurnDexterityPower extends PCLPower
{
    public static final String POWER_ID = CreateFullID(NextTurnDexterityPower.class);

    public NextTurnDexterityPower(AbstractCreature owner, int value)
    {
        super(owner, POWER_ID);

        this.amount = value;

        updateDescription();
    }

    @Override
    public void updateDescription()
    {
        String[] desc = powerStrings.DESCRIPTIONS;

        description = desc[0] + amount + desc[1];
    }

    @Override
    public void atStartOfTurnPostDraw()
    {
        super.atStartOfTurnPostDraw();

        PCLActions.Top.StackPower(new LoseDexterityPower(owner, amount))
        .ShowEffect(false, true)
        .AddCallback(() -> PCLActions.Top.StackPower(new DexterityPower(owner, amount)));

        RemovePower();
    }
}
