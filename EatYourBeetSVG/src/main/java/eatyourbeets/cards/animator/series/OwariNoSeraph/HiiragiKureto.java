package eatyourbeets.cards.animator.series.OwariNoSeraph;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Lightning;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class HiiragiKureto extends AnimatorCard
{
    public static final EYBCardData DATA = Register(HiiragiKureto.class)
            .SetAttack(1, CardRarity.UNCOMMON)
            .SetSeriesFromClassPackage();

    public HiiragiKureto()
    {
        super(DATA);

        Initialize(5, 0, 2);
        SetUpgrade(4, 0);

        SetAffinity_Thunder();
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        if (CombatStats.TryActivateLimited(cardID))
        {
            GameActions.Bottom.ExhaustFromHand(name, 1, true);
            GameActions.Bottom.ChannelOrbs(Lightning::new, magicNumber);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        int attackTimes = 1 + GameUtilities.GetOrbCount(Lightning.ORB_ID);

        for (int i=0; i<attackTimes; i++) {
            GameActions.Bottom.DealDamage(this, m, AttackEffects.SLASH_HEAVY);
        }
    }
}