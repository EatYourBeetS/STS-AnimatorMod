package eatyourbeets.cards.animator.basic;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;

public class Defend_Overlord extends Defend
{
    public static final String ID = Register(Defend_Overlord.class.getSimpleName());

    public Defend_Overlord()
    {
        super(ID, 1, CardTarget.SELF);

        Initialize(0, 5, 1);
        SetSynergy(Synergies.Overlord);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActions.Bottom.GainBlock(this.block);
        GameActions.Bottom.ModifyAllCombatInstances(uuid, c -> c.baseBlock += c.magicNumber);
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