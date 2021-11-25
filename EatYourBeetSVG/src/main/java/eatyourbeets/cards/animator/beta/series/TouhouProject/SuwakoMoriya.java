package eatyourbeets.cards.animator.beta.series.TouhouProject;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.orbs.animator.Earth;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.TargetHelper;

public class SuwakoMoriya extends AnimatorCard {
    public static final EYBCardData DATA = Register(SuwakoMoriya.class)
            .SetSkill(1, CardRarity.COMMON, EYBCardTarget.Normal)
            .SetSeriesFromClassPackage(true);
    public static final int MALLEABLE = 3;

    public SuwakoMoriya() {
        super(DATA);

        Initialize(0, 1, 7, 5);
        SetUpgrade(0, 0, 2, 2);
        SetAffinity_Green(1, 0, 1);
        SetAffinity_Orange(1, 0, 1);

        SetExhaust(true);
    }

    @Override
    public void OnUpgrade() {
        SetRetainOnce(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {
        GameActions.Bottom.GainBlock(block);

        if (CheckPrimaryCondition(true)) {
            GameActions.Bottom.ApplyConstricted(TargetHelper.Normal(m), secondaryValue);
        }
        else {
            GameActions.Bottom.ApplyPoison(TargetHelper.Normal(m), magicNumber);
        }

        if (GameUtilities.GetOrbCount(Earth.ORB_ID) >= MALLEABLE && info.TryActivateLimited()) {
            GameActions.Bottom.GainMalleable(MALLEABLE);
        }
    }

    @Override
    public boolean CheckPrimaryCondition(boolean tryUse)
    {
        return CombatStats.Affinities.GetAffinityLevel(Affinity.Red, true) > CombatStats.Affinities.GetAffinityLevel(Affinity.Green, true);
    }
}

