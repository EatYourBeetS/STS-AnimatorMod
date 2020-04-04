package eatyourbeets.monsters.UnnamedReign.Shapes.Cube;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import com.megacrit.cardcrawl.powers.WraithFormPower;
import eatyourbeets.monsters.EYBAbstractMove;
import eatyourbeets.monsters.EYBMoveset;
import eatyourbeets.monsters.SharedMoveset.EYBMove_Attack;
import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterElement;
import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterShape;
import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterTier;
import eatyourbeets.powers.monsters.UltimateCubePower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class UltimateCube extends Cube
{
    public static final String ID = CreateFullID(MonsterShape.Cube, MonsterElement.Ultimate, MonsterTier.Ultimate);
    public static final String NAME = "Ultimate Cube";

    private final EYBMove_Attack antiIntangible;
    private final EYBAbstractMove firstAttack;

    public UltimateCube()
    {
        super(MonsterElement.Ultimate, MonsterTier.Ultimate, 0, 0);

        antiIntangible = moveset.Special.Attack(6, 30);

        firstAttack = moveset.Special.AttackVulnerable(24, 2)
        .SetDamageScaling(0.2f)
        .SetUses(1);

        moveset.SetFindSpecialMove(roll ->
        {
            if (firstAttack.uses > 0)
            {
                return firstAttack;
            }
            else if (GameUtilities.GetPowerAmount(IntangiblePlayerPower.POWER_ID) >= 2 && !AbstractDungeon.player.hasPower(WraithFormPower.POWER_ID))
            {
                return antiIntangible;
            }
            else
            {
                return null;
            }
        });

        // Rotation:
        moveset.mode = EYBMoveset.Mode.Sequential;

        moveset.Normal.AttackWeak(4, 2)
        .SetDamageMultiplier(2)
        .SetDamageBonus(4, 3)
        .SetMiscBonus(18, 1);

        moveset.Normal.AttackFrail(4, 2)
        .SetDamageMultiplier(2)
        .SetDamageBonus(4, 3)
        .SetMiscBonus(18, 1);

        moveset.Normal.AttackVulnerable(4, 2)
        .SetDamageMultiplier(2)
        .SetDamageBonus(4, 3)
        .SetMiscBonus(18, 1);
    }

    @Override
    public void usePreBattleAction()
    {
        super.usePreBattleAction();

        GameActions.Bottom.ApplyPower(this, this, new UltimateCubePower(this, 12));
        GameActions.Bottom.StackPower(new ArtifactPower(this, 3));
    }
}