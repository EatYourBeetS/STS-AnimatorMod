package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;
import eatyourbeets.GameActionsHelper;

public class ThrowingKnife_2 extends ThrowingKnife
{
    public static final String ID = CreateFullID(ThrowingKnife_2.class.getSimpleName());

    public ThrowingKnife_2()
    {
        super(ID);

        Initialize(2,0, 2);
    }

    @Override
    protected void AddSecondaryEffect(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.ApplyPower(p, m, new PoisonPower(m, p, this.magicNumber), this.magicNumber);
    }
}