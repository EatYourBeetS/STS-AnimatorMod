package pinacolada.cards.pcl.series.Bleach;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.powers.PCLCombatStats;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class MayuriKurotsuchi extends PCLCard {
    public static final PCLCardData DATA = Register(MayuriKurotsuchi.class).SetSkill(1, CardRarity.COMMON, eatyourbeets.cards.base.EYBCardTarget.Normal).SetSeriesFromClassPackage();
    public static final int POISON_THRESHOLD = 16;

    public MayuriKurotsuchi() {
        super(DATA);

        Initialize(0, 1, 3, 3);
        SetUpgrade(0, 0, 1, 1);

        SetAffinity_Silver(1, 0, 0);
        SetAffinity_Orange(1, 0, 0);
        SetAffinity_Blue(0,0,1);
        SetAffinity_Dark(1);
    }

    @Override
    public int GetXValue() {
        return magicNumber + secondaryValue * PCLCombatStats.MatchingSystem.GetPowerLevel(PCLAffinity.Green);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {
        PCLActions.Bottom.GainBlock(block);
        PCLActions.Bottom.ApplyPoison(TargetHelper.Normal(m), GetXValue())
                .AddCallback(m, (enemy, cards) -> {
                    if (PCLGameUtilities.GetPowerAmount(enemy, PoisonPower.POWER_ID) >= POISON_THRESHOLD && info.TryActivateLimited()) {
                        PCLActions.Bottom.GainInspiration(1);
                    }
                });
    }
}