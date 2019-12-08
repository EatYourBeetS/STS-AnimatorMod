package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.cards.AnimatorCard_UltraRare;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.animator.AzrielPower;
import eatyourbeets.powers.common.PlayerFlightPower;
import eatyourbeets.utilities.GameActionsHelper2;

public class Azriel extends AnimatorCard_UltraRare
{
    public static final String ID = Register(Azriel.class.getSimpleName(), EYBCardBadge.Special);

    public Azriel()
    {
        super(ID, 3, CardType.POWER, CardTarget.SELF);

        Initialize(0,0, 1);

        SetEthereal(true);
        SetSynergy(Synergies.NoGameNoLife);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper2.StackPower(new PlayerFlightPower(p, 2));
        GameActionsHelper2.StackPower(new AzrielPower(p, this.magicNumber));
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            SetEthereal(false);
        }
    }
}