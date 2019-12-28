package eatyourbeets.cards.animator.basic;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;

public class Defend_GATE extends Defend
{
    public static final String ID = Register(Defend_GATE.class);

    public Defend_GATE()
    {
        super(ID, 1, CardTarget.SELF);

        Initialize(0, 6);
        SetUpgrade(0, 3);

        SetRetain(true);
        SetSynergy(Synergies.Gate);
    }

    @Override
    public void triggerOnEndOfTurnForPlayingCard()
    {
        super.triggerOnEndOfTurnForPlayingCard();

        SetRetain(true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainBlock(this.block);
    }
}