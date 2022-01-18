package pinacolada.cards.pcl.series.TouhouProject;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.powers.PCLCombatStats;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLJUtils;

public class SuwakoMoriya extends PCLCard {
    public static final PCLCardData DATA = Register(SuwakoMoriya.class)
            .SetSkill(1, CardRarity.COMMON, eatyourbeets.cards.base.EYBCardTarget.Normal)
            .SetSeriesFromClassPackage(true);
    public static final int MALLEABLE = 3;

    public SuwakoMoriya() {
        super(DATA);

        Initialize(0, 2, 7, 3);
        SetUpgrade(0, 0, 2, 1);
        SetAffinity_Green(1, 0, 1);
        SetAffinity_Orange(1, 0, 1);

        SetAffinityRequirement(PCLAffinity.Red, 5);

        SetExhaust(true);
    }

    @Override
    public void OnUpgrade() {
        SetRetainOnce(true);
    }

    @Override
    public int GetXValue() {
        return magicNumber + PCLJUtils.Min(PCLAffinity.Basic(), af -> PCLCombatStats.MatchingSystem.GetAffinityLevel(af,true));
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {
        PCLActions.Bottom.GainBlock(block);
        PCLActions.Bottom.ApplyPoison(TargetHelper.Normal(m), GetXValue());

        if (TrySpendAffinity(PCLAffinity.Red)) {
            PCLActions.Bottom.ApplyConstricted(TargetHelper.Normal(m), secondaryValue);
        }
    }
}

