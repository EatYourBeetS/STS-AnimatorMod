package pinacolada.cards.pcl.series.GenshinImpact;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Frost;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.actions.orbs.TriggerOrbPassiveAbility;
import pinacolada.cards.base.*;
import pinacolada.powers.PCLPowerHelper;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class KaeyaAlberich extends PCLCard {
    public static final PCLCardData DATA = Register(KaeyaAlberich.class).SetSkill(1, CardRarity.COMMON, PCLCardTarget.AoE).SetSeriesFromClassPackage();

    public KaeyaAlberich() {
        super(DATA);

        Initialize(0, 1, 2);
        SetUpgrade(0, 0, 0);
        SetAffinity_Orange(1, 0, 1);
        SetAffinity_Green(1, 0, 1);
        SetAffinity_Blue(1, 0, 0);

        SetAffinityRequirement(PCLAffinity.Blue, 3);
    }


    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {

        PCLActions.Bottom.GainBlock(block);

        if (TrySpendAffinity(PCLAffinity.Blue) && CombatStats.TryActivateSemiLimited(cardID)) {
            PCLActions.Bottom.StackPower(TargetHelper.Enemies(), PCLPowerHelper.Shackles, magicNumber);
        }

        PCLActions.Bottom.ChannelOrb(new Frost()).AddCallback(() -> {
            PCLActions.Bottom.Callback(new TriggerOrbPassiveAbility(1).SetFilter(PCLGameUtilities::IsCommonOrb));
            if (upgraded) {
                PCLActions.Bottom.Callback(new TriggerOrbPassiveAbility(1));
            }
        });
    }
}