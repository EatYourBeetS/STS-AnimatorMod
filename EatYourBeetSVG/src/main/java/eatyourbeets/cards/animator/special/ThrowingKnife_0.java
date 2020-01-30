package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.GameActions;

public class ThrowingKnife_0 extends ThrowingKnife
{
    public static final String ID = Register(ThrowingKnife_0.class);

    public ThrowingKnife_0()
    {
        super(ID);

        Initialize(2, 0, 1);
        SetUpgrade(3, 0);
    }

    @Override
    protected void AddSecondaryEffect(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Top.ApplyWeak(p, m, this.magicNumber);
    }
}