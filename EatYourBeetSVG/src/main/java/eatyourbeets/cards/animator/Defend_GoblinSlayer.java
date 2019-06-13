package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.GameActionsHelper;

public class Defend_GoblinSlayer extends Defend
{
    public static final String ID = CreateFullID(Defend_GoblinSlayer.class.getSimpleName());

    public Defend_GoblinSlayer()
    {
        super(ID, 1, CardTarget.SELF);

        this.exhaust = true;

        Initialize(0, 7);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper.GainBlock(p, this.block);
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