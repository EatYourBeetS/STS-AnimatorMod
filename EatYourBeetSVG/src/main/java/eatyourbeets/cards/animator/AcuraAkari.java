package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.common.TemporaryEnvenomPower;
import eatyourbeets.utilities.GameActionsHelper;

public class AcuraAkari extends AnimatorCard
{
    public static final String ID = CreateFullID(AcuraAkari.class.getSimpleName());

    public AcuraAkari()
    {
        super(ID, 0, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);

        Initialize(0, 0, 1);

        this.baseSecondaryValue = this.secondaryValue = 1;

        SetSynergy(Synergies.Chaika);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.ApplyPower(p, p, new TemporaryEnvenomPower(p, this.magicNumber), this.magicNumber);

        if (GetOtherCardsInHand().size() > 0)
        {
            GameActionsHelper.Discard(1, false);

            for (int i = 0; i < secondaryValue; i++)
            {
                GameActionsHelper.MakeCardInHand(ThrowingKnife.GetRandomCard(), 1, upgraded);
            }
        }
    }

    @Override
    public void upgrade()
    {
        TryUpgrade();
    }
}