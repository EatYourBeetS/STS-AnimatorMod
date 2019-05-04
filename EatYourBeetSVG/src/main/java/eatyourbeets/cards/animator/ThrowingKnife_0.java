package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import eatyourbeets.GameActionsHelper;

public class ThrowingKnife_0 extends ThrowingKnife
{
    public static final String ID = CreateFullID(ThrowingKnife_0.class.getSimpleName());

    public ThrowingKnife_0()
    {
        super(ID);

        Initialize(3,0, 1);
    }

    @Override
    protected void AddSecondaryEffect(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.ApplyPower(p, m, new WeakPower(m, this.magicNumber, false), this.magicNumber);
    }
}