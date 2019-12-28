package eatyourbeets.cards.animator.basic;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;

public class Defend_GoblinSlayer extends Defend
{
    public static final String ID = Register(Defend_GoblinSlayer.class);

    public Defend_GoblinSlayer()
    {
        super(ID, 1, CardTarget.SELF);

        Initialize(0, 7);
        SetUpgrade(0, 3);

        SetExhaust(true);
        SetSynergy(Synergies.GoblinSlayer);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainBlock(this.block);
    }
}