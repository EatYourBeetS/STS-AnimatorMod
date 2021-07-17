package eatyourbeets.cards.animator.basic;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.GameActions;

public class Defend_Dark extends Defend
{
    public static final String ID = Register(Defend_Dark.class).ID;

    public Defend_Dark()
    {
        super(ID, 1, CardTarget.SELF);

        Initialize(0, 5);
        SetUpgrade(0, 3);

        SetAffinity_Dark(1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.GainBlock(block);
    }
}