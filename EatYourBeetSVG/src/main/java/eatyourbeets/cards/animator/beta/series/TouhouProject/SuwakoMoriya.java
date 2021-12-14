package eatyourbeets.cards.animator.beta.series.TouhouProject;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JUtils;
import eatyourbeets.utilities.TargetHelper;

public class SuwakoMoriya extends AnimatorCard {
    public static final EYBCardData DATA = Register(SuwakoMoriya.class)
            .SetSkill(1, CardRarity.COMMON, EYBCardTarget.Normal)
            .SetSeriesFromClassPackage(true);
    public static final int MALLEABLE = 3;

    public SuwakoMoriya() {
        super(DATA);

        Initialize(0, 2, 7, 4);
        SetUpgrade(0, 0, 2, 1);
        SetAffinity_Green(1, 0, 1);
        SetAffinity_Orange(1, 0, 1);

        SetAffinityRequirement(Affinity.Red, 4);

        SetExhaust(true);
    }

    @Override
    public void OnUpgrade() {
        SetRetainOnce(true);
    }

    @Override
    public int GetXValue() {
        return magicNumber + JUtils.Min(Affinity.Basic(), af -> CombatStats.Affinities.GetAffinityLevel((Affinity) af,true));
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.ApplyPoison(TargetHelper.Normal(m), GetXValue());

        if (TrySpendAffinity(Affinity.Red)) {
            GameActions.Bottom.ApplyConstricted(TargetHelper.Normal(m), secondaryValue);
        }
    }
}

