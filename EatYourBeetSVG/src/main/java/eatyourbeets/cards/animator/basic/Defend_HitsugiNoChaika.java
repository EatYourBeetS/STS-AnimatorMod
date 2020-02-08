package eatyourbeets.cards.animator.basic;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;

public class Defend_HitsugiNoChaika extends Defend
{
    public static final String ID = Register(Defend_HitsugiNoChaika.class).ID;

    public Defend_HitsugiNoChaika()
    {
        super(ID, 1, CardTarget.SELF);

        Initialize(0, 5);
        SetUpgrade(0, 3);

        SetSynergy(Synergies.Chaika);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainBlock(this.block);
        GameActions.Bottom.Cycle(name, 1);
    }
}