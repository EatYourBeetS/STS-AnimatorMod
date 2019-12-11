package eatyourbeets.cards.animator.series.HitsugiNoChaika;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardBadge;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.common.TemporaryEnvenomPower;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.utilities.GameActions;

public class AcuraAkari extends AnimatorCard
{
    public static final String ID = Register(AcuraAkari.class.getSimpleName(), EYBCardBadge.Synergy);

    public AcuraAkari()
    {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);

        Initialize(0, 0, 2, 2);

        SetSynergy(Synergies.Chaika);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DiscardFromHand(name, 2, false)
        .SetOptions(false, false, false)
        .AddCallback(__ -> GameActions.Bottom.CreateThrowingKnives(magicNumber));

        if (HasActiveSynergy() && EffectHistory.TryActivateSemiLimited(cardID))
        {
            GameActions.Bottom.StackPower(new TemporaryEnvenomPower(p, secondaryValue));
        }
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeBaseCost(0);
        }
    }
}