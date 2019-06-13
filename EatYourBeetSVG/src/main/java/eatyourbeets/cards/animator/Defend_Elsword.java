package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.NextTurnBlockPower;
import eatyourbeets.utilities.GameActionsHelper;

public class Defend_Elsword extends Defend
{
    public static final String ID = CreateFullID(Defend_Elsword.class.getSimpleName());

    public Defend_Elsword()
    {
        super(ID, 1, CardTarget.SELF);

        Initialize(0, 5, 2);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper.GainBlock(p, this.block);
        GameActionsHelper.ApplyPower(p, p, new NextTurnBlockPower(p, this.magicNumber), this.magicNumber);
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeBlock(3);
        }
    }
}