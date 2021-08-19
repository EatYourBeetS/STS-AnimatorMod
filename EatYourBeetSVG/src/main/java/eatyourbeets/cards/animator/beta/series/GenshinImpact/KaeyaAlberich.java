package eatyourbeets.cards.animator.beta.series.GenshinImpact;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Frost;
import eatyourbeets.actions.orbs.TriggerOrbPassiveAbility;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.TargetHelper;

public class KaeyaAlberich extends AnimatorCard {
    public static final EYBCardData DATA = Register(KaeyaAlberich.class).SetSkill(1, CardRarity.COMMON, EYBCardTarget.ALL).SetSeriesFromClassPackage();

    public KaeyaAlberich() {
        super(DATA);

        Initialize(0, 0, 2);
        SetUpgrade(0, 2, 0);
        SetAffinity_Orange(1, 1, 0);
        SetAffinity_Green(1, 0, 0);
    }


    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing) {

        GameActions.Bottom.GainBlock(block);

        if (HasSynergy() && CombatStats.TryActivateSemiLimited(cardID)) {
            GameActions.Bottom.StackPower(TargetHelper.Enemies(), PowerHelper.Shackles, magicNumber);
        }

        GameActions.Bottom.ChannelOrb(new Frost()).AddCallback(() -> {
            AbstractOrb firstCommonOrb = null;
            for (AbstractOrb orb : player.orbs)
                if (GameUtilities.IsCommonOrb(orb)) {
                    firstCommonOrb = orb;
                    break;
                }

            if (firstCommonOrb != null) {
                GameActions.Bottom.Callback(new TriggerOrbPassiveAbility(1, false, false, firstCommonOrb));
            }
        });
    }
}