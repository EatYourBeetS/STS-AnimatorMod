package eatyourbeets.cards.animatorClassic.series.FullmetalAlchemist;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorClassicCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.SFX;
import eatyourbeets.orbs.animator.Earth;
import eatyourbeets.utilities.GameActions;

public class Scar extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(Scar.class).SetSeriesFromClassPackage().SetAttack(2, CardRarity.UNCOMMON, EYBAttackType.Elemental);

    public Scar()
    {
        super(DATA);

        Initialize(14, 0);
        SetUpgrade(4, 0);
        SetScaling(1, 0, 1);

        
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.NONE)
        .SetDamageEffect(__ -> SFX.Play(SFX.ORB_DARK_EVOKE, 0.7f, 0.75f));

        GameActions.Bottom.ExhaustFromHand(name, 1, true)
        .ShowEffect(true, true)
        .SetOptions(false, false, false)
        .AddCallback(() -> GameActions.Bottom.ChannelOrb(new Earth()));

        if (info.IsSynergizing)
        {
            GameActions.Bottom.FetchFromPile(name, 1, p.exhaustPile)
            .ShowEffect(true, true)
            .SetOptions(true, true);
            PurgeOnUseOnce();
        }
    }
}