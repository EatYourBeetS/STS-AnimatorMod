package eatyourbeets.powers.monsters;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.effects.SFX;
import eatyourbeets.monsters.EYBMonster;
import eatyourbeets.powers.AnimatorClickablePower;
import eatyourbeets.powers.PowerTriggerConditionType;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

public class ArcaneControllerPower extends AnimatorClickablePower
{
    public static final String POWER_ID = CreateFullID(ArcaneControllerPower.class);
    public static final int ENERGY_COST = 2;
    public static final int TURN_THRESHOLD = 4;

    public ArcaneControllerPower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID, PowerTriggerConditionType.Energy, ENERGY_COST);

        triggerCondition.SetUses(1, false, false);

        Initialize(amount, PowerType.DEBUFF, false);
    }

    @Override
    public String GetUpdatedDescription()
    {
        return FormatDescription(0, triggerCondition.requiredAmount, amount, TURN_THRESHOLD);
    }

    @Override
    public void OnUse(AbstractMonster m)
    {
        GameActions.Bottom.SFX(SFX.ORB_SLOT_GAIN, 0.75f, 0.75f);
        GameActions.Bottom.WaitRealtime(0.3f);
        GameActions.Bottom.Callback(() ->
        {
            final EYBMonster enemy = JUtils.SafeCast(owner, EYBMonster.class);
            if (enemy != null)
            {
                enemy.moveset.Special.Defend(amount).Select(true);

                final UltimateCubePower power = GameUtilities.GetPower(enemy, UltimateCubePower.class);
                if (power != null && power.amount > 0 && power.amount <= TURN_THRESHOLD)
                {
                    power.stackPower(1);
                }
            }
        });
        RemovePower();
    }
}