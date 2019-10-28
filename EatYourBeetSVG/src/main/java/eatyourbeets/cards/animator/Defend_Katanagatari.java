package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.powers.animator.EarthenThornsPower;

public class Defend_Katanagatari extends Defend
{
    public static final String ID = Register(Defend_Katanagatari.class.getSimpleName());

    public Defend_Katanagatari()
    {
        super(ID, 1, CardTarget.SELF);

        Initialize(0, 5, 2);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper.GainBlock(p, this.block);
        GameActionsHelper.ApplyPower(p, p, new EarthenThornsPower(p, this.magicNumber), this.magicNumber);
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