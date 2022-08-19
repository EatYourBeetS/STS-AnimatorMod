package eatyourbeets.cards.animatorClassic.series.FullmetalAlchemist;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.SFX;
import eatyourbeets.orbs.animator.Earth;
import eatyourbeets.utilities.GameActions;

public class Scar extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(Scar.class).SetAttack(2, CardRarity.UNCOMMON, EYBAttackType.Elemental);

    public Scar()
    {
        super(DATA);

        Initialize(14, 0);
        SetUpgrade(4, 0);
        SetScaling(1, 0, 1);

        SetSeries(CardSeries.FullmetalAlchemist);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.NONE)
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