package eatyourbeets.cards.animator.basic;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;

public class Defend_Overlord extends Defend
{
    public static final String ID = Register(Defend_Overlord.class).ID;

    public Defend_Overlord()
    {
        super(ID, 1, CardTarget.SELF);

        Initialize(0, 5, 1);
        SetUpgrade(0, 3);

        SetSynergy(Synergies.Overlord);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.ModifyAllInstances(uuid, c -> c.baseBlock += c.magicNumber);
    }
}