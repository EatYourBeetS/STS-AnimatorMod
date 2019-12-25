package eatyourbeets.cards.animator.basic;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.powers.animator.EarthenThornsPower;

public class Defend_Katanagatari extends Defend
{
    public static final String ID = Register(Defend_Katanagatari.class.getSimpleName());

    public Defend_Katanagatari()
    {
        super(ID, 1, CardTarget.SELF);

        Initialize(0, 5, 2);
        SetUpgrade(0, 3);

        SetSynergy(Synergies.Katanagatari);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainBlock(this.block);
        GameActions.Bottom.StackPower(new EarthenThornsPower(p, this.magicNumber));
    }
}