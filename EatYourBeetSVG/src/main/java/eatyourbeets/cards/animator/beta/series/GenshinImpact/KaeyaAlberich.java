package eatyourbeets.cards.animator.beta.series.GenshinImpact;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Frost;
import eatyourbeets.actions.orbs.TriggerOrbPassiveAbility;
import eatyourbeets.cards.base.*;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.TargetHelper;

public class KaeyaAlberich extends AnimatorCard {
    public static final EYBCardData DATA = Register(KaeyaAlberich.class).SetSkill(1, CardRarity.COMMON, EYBCardTarget.ALL).SetSeriesFromClassPackage();

    public KaeyaAlberich() {
        super(DATA);

        Initialize(0, 1, 2);
        SetUpgrade(0, 0, 0);
        SetAffinity_Orange(1, 0, 1);
        SetAffinity_Green(1, 0, 1);
        SetAffinity_Blue(1, 0, 0);

        SetAffinityRequirement(Affinity.Green, 4);
    }


    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {

        GameActions.Bottom.GainBlock(block);

        if (TrySpendAffinity(Affinity.Green) && CombatStats.TryActivateSemiLimited(cardID)) {
            GameActions.Bottom.StackPower(TargetHelper.Enemies(), PowerHelper.Shackles, magicNumber);
        }

        GameActions.Bottom.ChannelOrb(new Frost()).AddCallback(() -> {
            GameActions.Bottom.Callback(new TriggerOrbPassiveAbility(1).SetFilter(GameUtilities::IsCommonOrb));
            if (upgraded) {
                GameActions.Bottom.Callback(new TriggerOrbPassiveAbility(1));
            }
        });
    }
}