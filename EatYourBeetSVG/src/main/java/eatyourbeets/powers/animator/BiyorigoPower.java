package eatyourbeets.powers.animator;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.PowerIconShowEffect;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.common.ForcePower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

public class BiyorigoPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(BiyorigoPower.class.getSimpleName());

    public BiyorigoPower(AbstractCreature owner)
    {
        super(owner, POWER_ID);

        this.amount = -1;

        updateDescription();
    }

    @Override
    public void atEndOfTurn(boolean isPlayer)
    {
        super.atEndOfTurn(isPlayer);

        if (AbstractDungeon.player.energy.energy > 0)
        {
            int force = GameUtilities.GetPowerAmount(ForcePower.POWER_ID);
            if (force > 0)
            {
                AbstractDungeon.player.energy.use(1);
                GameActions.Bottom.GainForce(force);
                GameEffects.Queue.Add(new PowerIconShowEffect(this));
                flash();
            }
        }
    }
}
