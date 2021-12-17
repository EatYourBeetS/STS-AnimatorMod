package pinacolada.cards.pcl.special;

import com.megacrit.cardcrawl.orbs.AbstractOrb;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.PCLCardData;
import pinacolada.orbs.pcl.Water;
import pinacolada.powers.PowerHelper;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLActions;

public class OrbCore_Water extends OrbCore
{
    public static final PCLCardData DATA = RegisterOrbCore(OrbCore_Water.class, GR.Tooltips.Water, GR.Tooltips.Wisdom, GR.Tooltips.Affinity_Blue)
            .SetPower(3, CardRarity.SPECIAL)
            .SetColor(CardColor.COLORLESS);

    public OrbCore_Water()
    {
        super(DATA, 1, 7);

        SetAffinity_Blue(2);
        SetHealing(true);
    }

    @Override
    public Class<? extends AbstractOrb> GetOrb() {
        return Water.class;
    }

    @Override
    public boolean EvokeEffect(OrbCorePower power) {
        if (CombatStats.TryActivateLimited(cardID, 2)) {
            PCLActions.Bottom.StackPower(TargetHelper.Player(), PowerHelper.Vitality, power.amount);
            return true;
        }
        return false;
    }
}