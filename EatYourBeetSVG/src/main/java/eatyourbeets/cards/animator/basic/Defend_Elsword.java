package eatyourbeets.cards.animator.basic;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.NextTurnBlockPower;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;

public class Defend_Elsword extends Defend
{
    public static final String ID = Register(Defend_Elsword.class).ID;

    public Defend_Elsword()
    {
        super(ID, 1, CardTarget.SELF);

        Initialize(0, 5, 2);
        SetUpgrade(0, 3);

        SetSynergy(Synergies.Elsword);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainBlock(this.block);
        GameActions.Bottom.StackPower(new NextTurnBlockPower(p, this.magicNumber));
    }
}