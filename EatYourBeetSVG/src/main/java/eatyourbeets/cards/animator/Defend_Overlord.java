package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.common.ModifyBlockActionWhichActuallyWorks;
import eatyourbeets.cards.Synergies;
import eatyourbeets.utilities.GameActionsHelper;

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
        GameActionsHelper.GainBlock(p, this.block);
        GameActionsHelper.AddToBottom(new ModifyBlockActionWhichActuallyWorks(this.uuid, this.magicNumber));
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