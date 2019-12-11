package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCardBadge;
import eatyourbeets.utilities.GameActions;

public class ThrowingKnife_1 extends ThrowingKnife
{
    public static final String ID = Register(ThrowingKnife_1.class.getSimpleName(), EYBCardBadge.Discard);

    public ThrowingKnife_1()
    {
        super(ID);

        Initialize(2,0, 1);
    }

    @Override
    protected void AddSecondaryEffect(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Top.ApplyVulnerable(p, m, this.magicNumber);
    }
}