package eatyourbeets.cards.animator.beta.series.GenshinImpact;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Frost;
import com.megacrit.cardcrawl.orbs.Lightning;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.orbs.animator.Fire;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class KaeyaAlberich extends AnimatorCard {
    public static final EYBCardData DATA = Register(KaeyaAlberich.class).SetSkill(1, CardRarity.COMMON, EYBCardTarget.ALL).SetSeriesFromClassPackage();

    public KaeyaAlberich() {
        super(DATA);

        Initialize(0, 0, 2);
        SetAffinity_Red(1, 0, 0);
        SetAffinity_Green(1, 1, 0);
    }


    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing) {

        GameActions.Bottom.GainBlock(block);

        if (HasSynergy() && CombatStats.TryActivateSemiLimited(cardID))
        {
            for (AbstractMonster enemy : GameUtilities.GetEnemies(true))
            {
                GameActions.Bottom.ReduceStrength(enemy, magicNumber, true);
            }
        }

        AbstractOrb firstCommonOrb = null;
        for (AbstractOrb orb : p.orbs)
            if (Fire.ORB_ID.equals(orb.ID) || Frost.ORB_ID.equals(orb.ID) || Lightning.ORB_ID.equals(orb.ID)) {
                firstCommonOrb = orb;
                break;
            }

        if (firstCommonOrb != null) {
            firstCommonOrb.onStartOfTurn();
            firstCommonOrb.onEndOfTurn();
        }

        GameActions.Bottom.ChannelOrb(new Frost());
    }
}