package eatyourbeets.cards.animator.basic;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Defend_Fate extends Defend
{
    public static final String ID = Register(Defend_Fate.class).ID;

    public Defend_Fate()
    {
        super(ID, 1, CardTarget.SELF);

        Initialize(0, 3, 2);
        SetUpgrade(0, 3);

        SetSynergy(Synergies.Fate);
    }

    @Override
    protected float GetInitialBlock()
    {
        return super.GetInitialBlock() + GameUtilities.GetAllEnemies(true).size() * magicNumber;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainBlock(this.block);
    }
}