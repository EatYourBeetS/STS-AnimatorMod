package eatyourbeets.cards.animator.series.FullmetalAlchemist;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.orbs.animator.Earth;
import eatyourbeets.utilities.GameActions;

public class Scar extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Scar.class)
            .SetAttack(2, CardRarity.UNCOMMON, EYBAttackType.Elemental)
            .SetSeriesFromClassPackage();

    public Scar()
    {
        super(DATA);

        Initialize(14, 0);
        SetUpgrade(4, 0);

        SetAffinity_Red(1);
        SetAffinity_Blue(1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.NONE)
        .SetDamageEffect(__ -> CardCrawlGame.sound.playA("ORB_DARK_EVOKE", -0.3f));

        GameActions.Bottom.ExhaustFromHand(name, 1, true)
        .ShowEffect(true, true)
        .SetOptions(false, false, false)
        .AddCallback(() -> GameActions.Bottom.ChannelOrb(new Earth()));

        if (isSynergizing)
        {
            GameActions.Bottom.FetchFromPile(name, 1, p.exhaustPile)
            .ShowEffect(true, true)
            .SetOptions(true, true);
            PurgeOnUseOnce();
        }
    }
}