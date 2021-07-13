package eatyourbeets.cards.animator.basic;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.utilities.GameActions;

public class Defend_GoblinSlayer extends Defend
{
    public static final String ID = Register(Defend_GoblinSlayer.class).ID;

    public Defend_GoblinSlayer()
    {
        super(ID, 1, CardTarget.SELF);

        Initialize(0, 7);
        SetUpgrade(0, 3);

        SetExhaust(true);
        SetSeries(CardSeries.GoblinSlayer);
        SetAffinity_Red(1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.GainBlock(block);
    }
}