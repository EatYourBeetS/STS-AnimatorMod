package eatyourbeets.cards.animator.basic;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;

public class Defend_AccelWorld extends Defend
{
    public static final String ID = Register(Defend_AccelWorld.class).ID;

    public Defend_AccelWorld()
    {
        super(ID, 1, CardTarget.SELF);

        Initialize(0, 5, 3);
        SetUpgrade(0, 3);

        SetSynergy(Synergies.AccelWorld);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        GameActions.Bottom.GainBlock(magicNumber);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.GainBlock(block);
    }
}