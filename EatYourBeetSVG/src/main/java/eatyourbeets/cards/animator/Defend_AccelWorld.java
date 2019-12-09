package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.cards.Synergies;
import eatyourbeets.utilities.GameActions;

public class Defend_AccelWorld extends Defend
{
    public static final String ID = Register(Defend_AccelWorld.class.getSimpleName(), EYBCardBadge.Discard);

    public Defend_AccelWorld()
    {
        super(ID, 1, CardTarget.SELF);

        Initialize(0, 5, 3);

        SetSynergy(Synergies.AccelWorld);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        GameActions.Bottom.GainBlock(this.magicNumber);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActions.Bottom.GainBlock(this.block);
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