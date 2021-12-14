package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.orbs.AbstractOrb;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.orbs.animator.Water;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.TargetHelper;

public class OrbCore_Water extends OrbCore
{
    public static final EYBCardData DATA = RegisterOrbCore(OrbCore_Water.class, GR.Tooltips.Water, GR.Tooltips.Wisdom, GR.Tooltips.Affinity_Blue)
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
            GameActions.Bottom.StackPower(TargetHelper.Player(), PowerHelper.Vitality, power.amount);
            return true;
        }
        return false;
    }
}