package eatyourbeets.powers.deprecated;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.LoseDexterityPower;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;

public class NextTurnDexterityPower extends AnimatorPower
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

        GameActions.Top.StackPower(new LoseDexterityPower(owner, amount))
        .ShowEffect(false, true)
        .AddCallback(() -> GameActions.Top.StackPower(new DexterityPower(owner, amount)));

        RemovePower();
    }
}
