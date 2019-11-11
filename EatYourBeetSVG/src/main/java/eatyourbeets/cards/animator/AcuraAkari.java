package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.powers.common.TemporaryEnvenomPower;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.utilities.GameActionsHelper;

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
        if (GetOtherCardsInHand().size() >= 1)
        {
            GameActionsHelper.Discard(1, false);

            for (int i = 0; i < magicNumber; i++)
            {
                GameActionsHelper.MakeCardInHand(ThrowingKnife.GetRandomCard(), 1, false);
            }
        }

        if (HasActiveSynergy() && EffectHistory.TryActivateSemiLimited(cardID))
        {
            GameActionsHelper.ApplyPower(p, p, new TemporaryEnvenomPower(p, secondaryValue), secondaryValue);
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