package eatyourbeets.cards.animator.basic;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.NextTurnBlockPower;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.utilities.GameActions;

public class Defend_Elsword extends Defend
{
    public static final String ID = Register(Defend_Elsword.class).ID;

    public Defend_Elsword()
    {
        super(ID, 1, CardTarget.SELF);

        Initialize(0, 5, 2);
        SetUpgrade(0, 3);

        SetSeries(CardSeries.Elsword);
        SetAffinity_Green(1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.StackPower(new NextTurnBlockPower(p, magicNumber));
    }
}