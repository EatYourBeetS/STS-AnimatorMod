package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCardBadge;
import eatyourbeets.utilities.GameActions;

public class ThrowingKnife_2 extends ThrowingKnife
{
    public static final String ID = Register(ThrowingKnife_2.class, EYBCardBadge.Discard);

    public ThrowingKnife_2()
    {
        super(ID);

        Initialize(2, 0, 2);
        SetUpgrade(3, 0);
    }

    @Override
    protected void AddSecondaryEffect(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Top.ApplyPoison(p, m, magicNumber);
    }
}