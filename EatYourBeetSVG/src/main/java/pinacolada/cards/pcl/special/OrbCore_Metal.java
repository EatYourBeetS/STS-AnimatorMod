package pinacolada.cards.pcl.special;

import com.megacrit.cardcrawl.orbs.AbstractOrb;
import eatyourbeets.powers.CombatStats;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCardData;
import pinacolada.orbs.pcl.Metal;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLActions;

public class OrbCore_Metal extends OrbCore
{
    public static final PCLCardData DATA = RegisterOrbCore(OrbCore_Metal.class, GR.Tooltips.Metal, GR.Tooltips.Technic, GR.Tooltips.Affinity_Silver)
            .SetPower(3, CardRarity.SPECIAL)
            .SetColor(CardColor.COLORLESS);

    public OrbCore_Metal()
    {
        super(DATA, 2, 7);

        SetAffinity_Silver(1);
    }

    @Override
    public Class<? extends AbstractOrb> GetOrb() {
        return Metal.class;
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