package eatyourbeets.monsters.UnnamedReign.Shapes.Wisp;

import com.megacrit.cardcrawl.powers.HexPower;
import eatyourbeets.monsters.EYBMoveset;
import eatyourbeets.monsters.SharedMoveset.special.EYBMove_Buff_StrengthAndArtifact;
import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterElement;
import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterShape;
import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterTier;
import eatyourbeets.powers.monsters.UltimateWispPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class UltimateWisp extends Wisp
{
    public static final String ID = CreateFullID(MonsterShape.Wisp, MonsterElement.Ultimate, MonsterTier.Ultimate);
    public static final String NAME = "Ultimate Wisp";

    public UltimateWisp()
    {
        super(MonsterElement.Ultimate, MonsterTier.Ultimate, 0, 0);

        moveset.mode = EYBMoveset.Mode.Sequential;

        moveset.Normal.Attack(8, 3)
        .SetDamageScaling(0.2f);

        moveset.Normal.Add(new EYBMove_Buff_StrengthAndArtifact(3, 1));

        moveset.Normal.AttackFrail(2, 2)
        .SetDamageMultiplier(8);

        moveset.Normal.AttackDebuff(6, 4)
        .SetDamageScaling(0.2f)
        .SetOnUse((m, t) ->
        {
            if (GameUtilities.GetAscensionLevel() >= 18 || !t.hasPower(HexPower.POWER_ID))
            {
                GameActions.Bottom.StackPower(this, new HexPower(t, 1));
            }
        });
    }

    @Override
    public void usePreBattleAction()
    {
        super.usePreBattleAction();

        GameActions.Bottom.ApplyPower(this, this, new UltimateWispPower(this));
    }
}