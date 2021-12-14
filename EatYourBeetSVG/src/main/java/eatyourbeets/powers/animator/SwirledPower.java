package eatyourbeets.powers.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import eatyourbeets.effects.SFX;
import eatyourbeets.interfaces.listeners.OnTryApplyPowerListener;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.powers.common.BurningPower;
import eatyourbeets.powers.common.ElectrifiedPower;
import eatyourbeets.powers.common.FreezingPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.TargetHelper;

import java.util.ArrayList;

public class SwirledPower extends AnimatorPower implements OnTryApplyPowerListener
{
    public static final String POWER_ID = CreateFullID(SwirledPower.class);

    public SwirledPower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);

        Initialize(amount, PowerType.DEBUFF, false);
    }

    @Override
    public void onInitialApplication()
    {
        super.onInitialApplication();

        Swirl();
    }

    @Override
    public void playApplyPowerSfx()
    {
        SFX.Play(SFX.WIND, 2.1f, 2.3f);
    }

    public void Swirl() {
        ArrayList<PowerHelper> powerHelpers = new ArrayList<>();
        if (GameUtilities.GetPowerAmount(owner, BurningPower.POWER_ID) > 0) {
            powerHelpers.add(PowerHelper.Burning);
        }
        if (GameUtilities.GetPowerAmount(owner, FreezingPower.POWER_ID) > 0) {
            powerHelpers.add(PowerHelper.Freezing);
        }
        if (GameUtilities.GetPowerAmount(owner, ElectrifiedPower.POWER_ID) > 0) {
            powerHelpers.add(PowerHelper.Electrified);
        }

        if (powerHelpers.size() > 0) {
            this.flash();
        }

        ArrayList<AbstractMonster> enemies = GameUtilities.GetEnemies(true);
        if (amount > 0) {
            if (GameUtilities.IsPlayer(owner) || enemies.size() == 1) {
                for (PowerHelper ph : powerHelpers) {
                    GameActions.Delayed.StackPower(TargetHelper.Normal(owner), ph, amount);
                }
            }
            else {
                for (AbstractMonster enemy : enemies) {
                    if (enemy != owner) {
                        for (PowerHelper ph : powerHelpers) {
                            GameActions.Delayed.StackPower(TargetHelper.Normal(enemy), ph, amount);
                        }
                    }
                }
            }
        }

        if (powerHelpers.size() > 0) {
            RemovePower();
        }
}

    @Override
    public boolean TryApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source, AbstractGameAction action) {
        if (target == owner && (BurningPower.POWER_ID.equals(power.ID) || FreezingPower.POWER_ID.equals(power.ID) || ElectrifiedPower.POWER_ID.equals(power.ID))) {
            GameActions.Bottom.Callback(this::Swirl);
        }
        return true;
    }
}