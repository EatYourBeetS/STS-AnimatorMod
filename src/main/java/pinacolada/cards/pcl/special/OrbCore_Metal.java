package pinacolada.cards.pcl.special;

import eatyourbeets.powers.CombatStats;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCardData;
import pinacolada.orbs.PCLOrbHelper;
import pinacolada.utilities.PCLActions;

public class OrbCore_Metal extends OrbCore
{
    public static final PCLCardData DATA = RegisterOrbCore(OrbCore_Metal.class, PCLOrbHelper.Metal)
            .SetPower(3, CardRarity.SPECIAL)
            .SetColor(CardColor.COLORLESS);

    public OrbCore_Metal()
    {
        super(DATA, 2, 7);

        SetAffinity_Silver(1);
    }

    @Override
    public boolean EvokeEffect(OrbCorePower power) {
        if (CombatStats.TryActivateLimited(cardID, 2)) {
            PCLActions.Bottom.StackAffinityPower(PCLAffinity.Silver, power.amount);
            return true;
        }
        return false;
    }
}