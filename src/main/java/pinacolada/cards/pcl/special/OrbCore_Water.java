package pinacolada.cards.pcl.special;

import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.PCLCardData;
import pinacolada.orbs.PCLOrbHelper;
import pinacolada.powers.PCLPowerHelper;
import pinacolada.utilities.PCLActions;

public class OrbCore_Water extends OrbCore
{
    public static final PCLCardData DATA = RegisterOrbCore(OrbCore_Water.class, PCLOrbHelper.Water)
            .SetPower(3, CardRarity.SPECIAL)
            .SetColor(CardColor.COLORLESS);

    public OrbCore_Water()
    {
        super(DATA, 2, 7);

        SetAffinity_Blue(1);
        SetHealing(true);
    }

    @Override
    public boolean EvokeEffect(OrbCorePower power) {
        if (CombatStats.TryActivateLimited(cardID, 2)) {
            PCLActions.Bottom.StackPower(TargetHelper.Player(), PCLPowerHelper.Vitality, power.amount);
            return true;
        }
        return false;
    }
}