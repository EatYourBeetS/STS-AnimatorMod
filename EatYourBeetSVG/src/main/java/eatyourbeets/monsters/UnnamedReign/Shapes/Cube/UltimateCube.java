package eatyourbeets.monsters.UnnamedReign.Shapes.Cube;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import com.megacrit.cardcrawl.powers.WraithFormPower;
import eatyourbeets.blights.animator.UltimateCubeBlight;
import eatyourbeets.monsters.EYBAbstractMove;
import eatyourbeets.monsters.EYBMoveset;
import eatyourbeets.monsters.SharedMoveset.EYBMove_Attack;
import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterElement;
import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterShape;
import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterTier;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.powers.monsters.ArcaneControllerPower;
import eatyourbeets.powers.monsters.LaserDefensePower;
import eatyourbeets.powers.monsters.UltimateCubePower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class UltimateCube extends Cube
{
    public static final String ID = CreateFullID(MonsterShape.Cube, MonsterElement.Ultimate, MonsterTier.Ultimate);
    public static final String NAME = "Ultimate Cube";

    private final EYBMove_Attack antiIntangible;
    private final EYBAbstractMove firstAttack;

    public UltimateCube(float x, float y)
    {
        super(MonsterElement.Ultimate, MonsterTier.Ultimate, x, y);

        antiIntangible = moveset.Special.Attack(6, 30);

        firstAttack = moveset.Special.AttackDebuff(24, PowerHelper.Vulnerable, 2)
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

        moveset.Normal.AttackDebuff(5, PowerHelper.Weak, 2)
        .SetNumberOfAttacks(2)
        .SetDamageBonus(4, 2);

        moveset.Normal.AttackDebuff(18, PowerHelper.Frail, 2)
        .SetDamageBonus(4, 2);

        moveset.Normal.AttackDebuff(8, PowerHelper.Vulnerable, 2)
        .SetNumberOfAttacks(2)
        .SetDamageBonus(4, 2);
    }

    @Override
    public void usePreBattleAction()
    {
        super.usePreBattleAction();

        GameActions.Bottom.ApplyPower(this, this, new ArcaneControllerPower(this, 30))
        .IgnoreArtifact(true).ShowEffect(false, true);
        GameActions.Bottom.ApplyPower(this, this, new UltimateCubePower(this, 12));
        GameActions.Bottom.StackPower(new ArtifactPower(this, UltimateCubeBlight.ID.equals(GameUtilities.GetAscensionBlightChoice()) ? 5 : 3));

        if (GameUtilities.GetAscensionLevel() >= 17)
        {
            GameActions.Bottom.StackPower(new LaserDefensePower(this, 1));
        }
    }

    @Override
    public void die(boolean triggerRelics)
    {
        final UltimateCubePower power = GameUtilities.GetPower(this, UltimateCubePower.POWER_ID);
        if (power == null || !power.enabled)
        {
            super.die(triggerRelics);
        }
    }
}