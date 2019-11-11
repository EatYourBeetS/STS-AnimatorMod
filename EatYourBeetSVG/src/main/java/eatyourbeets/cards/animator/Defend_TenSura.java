package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.Synergies;
import eatyourbeets.utilities.GameActionsHelper;

public class Defend_TenSura extends Defend
{
    public static final String ID = Register(Defend_TenSura.class.getSimpleName());

    public Defend_TenSura()
    {
        super(ID, 1, CardTarget.SELF);

        Initialize(0, 3);
        SetSynergy(Synergies.TenSura);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper.GainBlock(p, this.block);
        GameActionsHelper.GainBlock(p, this.block);
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeBlock(1);
        }
    }
}