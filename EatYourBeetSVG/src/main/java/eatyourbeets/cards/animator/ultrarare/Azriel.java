package eatyourbeets.cards.animator.ultrarare;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard_UltraRare;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.animator.AzrielPower;
import eatyourbeets.powers.common.PlayerFlightPower;
import eatyourbeets.utilities.GameActions;

public class Azriel extends AnimatorCard_UltraRare
{
    public static final String ID = Register_Old(Azriel.class);

    public Azriel()
    {
        super(ID, 3, CardType.POWER, CardTarget.SELF);

        Initialize(0, 0, 1);

        SetEthereal(true);
        SetSynergy(Synergies.NoGameNoLife);
    }

    @Override
    protected void OnUpgrade()
    {
        SetEthereal(false);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.StackPower(new PlayerFlightPower(p, 2));
        GameActions.Bottom.StackPower(new AzrielPower(p, this.magicNumber));
    }
}