package eatyourbeets.powers.unnamed;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.powers.UnnamedPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class MarkedPower extends UnnamedPower
{
    public static final String POWER_ID = CreateFullID(MarkedPower.class);

    public MarkedPower(AbstractCreature owner)
    {
        super(owner, POWER_ID);

        Initialize(-1, PowerType.DEBUFF, false);
    }

    @Override
    public void onInitialApplication()
    {
        super.onInitialApplication();

        for (AbstractMonster m : GameUtilities.GetEnemies(true))
        {
            if (m != owner && m.hasPower(MarkedPower.POWER_ID))
            {
                GameActions.Bottom.RemovePower(owner, m, MarkedPower.POWER_ID);
            }
        }
    }
}
