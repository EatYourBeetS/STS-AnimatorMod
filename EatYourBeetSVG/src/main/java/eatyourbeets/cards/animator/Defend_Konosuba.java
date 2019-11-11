package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.Synergies;
import eatyourbeets.utilities.GameActionsHelper;

public class Defend_Konosuba extends Defend
{
    public static final String ID = Register(Defend_Konosuba.class.getSimpleName());

    public Defend_Konosuba()
    {
        super(ID, 1, CardTarget.SELF);

        Initialize(0, 4, 2);

        SetSynergy(Synergies.Konosuba);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper.GainBlock(p, this.block);
        GameActionsHelper.GainTemporaryHP(p, magicNumber);
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