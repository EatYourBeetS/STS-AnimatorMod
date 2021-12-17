package pinacolada.powers.special;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import eatyourbeets.interfaces.listeners.OnTryApplyPowerListener;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.effects.SFX;
import pinacolada.powers.PCLPower;
import pinacolada.powers.PowerHelper;
import pinacolada.powers.common.BurningPower;
import pinacolada.powers.common.ElectrifiedPower;
import pinacolada.powers.common.FreezingPower;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

import java.util.ArrayList;

public class SwirledPower extends PCLPower implements OnTryApplyPowerListener
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
        if (PCLGameUtilities.GetPowerAmount(owner, BurningPower.POWER_ID) > 0) {
            powerHelpers.add(PowerHelper.Burning);
        }
        if (PCLGameUtilities.GetPowerAmount(owner, FreezingPower.POWER_ID) > 0) {
            powerHelpers.add(PowerHelper.Freezing);
        }
        if (PCLGameUtilities.GetPowerAmount(owner, ElectrifiedPower.POWER_ID) > 0) {
            powerHelpers.add(PowerHelper.Electrified);
        }

        if (powerHelpers.size() > 0) {
            this.flash();
        }

        ArrayList<AbstractMonster> enemies = PCLGameUtilities.GetEnemies(true);
        if (amount > 0) {
            if (PCLGameUtilities.IsPlayer(owner) || enemies.size() == 1) {
                for (PowerHelper ph : powerHelpers) {
                    PCLActions.Delayed.StackPower(TargetHelper.Normal(owner), ph, amount);
                }
            }
            else {
                for (AbstractMonster enemy : enemies) {
                    if (enemy != owner) {
                        for (PowerHelper ph : powerHelpers) {
                            PCLActions.Delayed.StackPower(TargetHelper.Normal(enemy), ph, amount);
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
            PCLActions.Bottom.Callback(this::Swirl);
        }
        return true;
    }
}