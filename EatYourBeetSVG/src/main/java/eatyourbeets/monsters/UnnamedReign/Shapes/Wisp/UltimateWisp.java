package eatyourbeets.monsters.UnnamedReign.Shapes.Wisp;

import com.megacrit.cardcrawl.powers.HexPower;
import eatyourbeets.monsters.EYBAbstractMove;
import eatyourbeets.monsters.EYBMoveset;
import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterElement;
import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterShape;
import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterTier;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.powers.monsters.UltimateWispPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class UltimateWisp extends Wisp
{
    public static final String ID = CreateFullID(MonsterShape.Wisp, MonsterElement.Ultimate, MonsterTier.Ultimate);
    public static final String NAME = "Ultimate Wisp";

    private final EYBAbstractMove antiStrengthLoss;
    private int antiStrengthLossCounter = 0;

    public UltimateWisp()
    {
        super(MonsterElement.Ultimate, MonsterTier.Ultimate, 0, 0);

        moveset.mode = EYBMoveset.Mode.Sequential;

        antiStrengthLoss = moveset.Special.Attack(50)
        .SetOnUse((m, t) -> m.damage.baseAmount += 10);

        moveset.Normal.Attack(8, 3)
        .SetDamageScaling(0.2f);

        moveset.Normal.Buff(PowerHelper.Strength, 3)
        .AddPower(PowerHelper.Artifact, 1);

        moveset.Normal.AttackDebuff(2, 8, PowerHelper.Frail, 2);

        moveset.Normal.Attack(6, 4)
        .SetIntent(Intent.ATTACK_DEBUFF)
        .SetDamageScaling(0.2f)
        .SetOnUse((m, t) ->
        {
            if (GameUtilities.GetAscensionLevel() >= 19 || !t.hasPower(HexPower.POWER_ID))
            {
                GameActions.Bottom.StackPower(this, new HexPower(t, 1));
            }
        });
    }

    @Override
    protected void SetNextMove(int roll, int historySize)
    {
        if (antiStrengthLossCounter > 3)
        {
            antiStrengthLoss.Select();
        }
        else
        {
            super.SetNextMove(roll, historySize);
        }
    }

    @Override
    public void takeTurn()
    {
        if (GameUtilities.IsAttacking(intent) && ((getIntentDmg() == 0) || (getIntentDmg() < (getIntentBaseDmg() - 5))))
        {
            antiStrengthLossCounter += 1;
        }

        super.takeTurn();
    }

    @Override
    public void usePreBattleAction()
    {
        super.usePreBattleAction();

        GameActions.Bottom.ApplyPower(this, this, new UltimateWispPower(this));
    }
}