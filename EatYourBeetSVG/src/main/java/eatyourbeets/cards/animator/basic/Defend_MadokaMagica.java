package eatyourbeets.cards.animator.basic;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.curse.Curse_GriefSeed;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;

public class Defend_MadokaMagica extends Defend
{
    public static final String ID = Register(Defend_MadokaMagica.class).ID;

    public Defend_MadokaMagica()
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

        if (Math.random() < 0.5)
        {
            GameActions.Bottom.MakeCardInDiscardPile(new Curse_GriefSeed());
        }
    }
}