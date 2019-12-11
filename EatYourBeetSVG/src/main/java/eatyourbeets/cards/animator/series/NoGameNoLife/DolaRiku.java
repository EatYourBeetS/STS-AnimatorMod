package eatyourbeets.cards.animator.series.NoGameNoLife;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.animator.DolaRikuAction;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardBadge;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.*;

public class DolaRiku extends AnimatorCard
{
    public static final String ID = Register(DolaRiku.class.getSimpleName(), EYBCardBadge.Synergy);

    public DolaRiku()
    {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);

        Initialize(0,0,2);

        SetSynergy(Synergies.NoGameNoLife);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActions.Bottom.ExhaustFromHand(name, 1, false)
        .SetOptions(false, false, false)
        .AddCallback(cards -> GameActions.Bottom.Add(new DolaRikuAction(cards.get(0), magicNumber)));

        if (HasActiveSynergy())
        {
            GameActions.Top.Draw(1)
            .SetFilter(c -> c.costForTurn == 0 && !GameUtilities.IsCurseOrStatus(c), false);
        }
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeMagicNumber(1);
        }
    }
}