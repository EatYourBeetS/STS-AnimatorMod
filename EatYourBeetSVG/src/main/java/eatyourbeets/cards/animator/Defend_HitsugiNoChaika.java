package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.GameActionsHelper;

public class Defend_HitsugiNoChaika extends Defend
{
    public static final String ID = CreateFullID(Defend_HitsugiNoChaika.class.getSimpleName());

    public Defend_HitsugiNoChaika()
    {
        super(ID, 1, CardTarget.SELF);

        Initialize(0, 5);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper.GainBlock(p, this.block);
        GameActionsHelper.CycleCardAction(1);
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