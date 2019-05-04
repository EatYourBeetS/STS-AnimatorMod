package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.FlightPower;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.AnimatorCard_UltraRare;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.AzrielPower;
import eatyourbeets.powers.PlayerFlightPower;

public class Azriel extends AnimatorCard_UltraRare
{
    public static final String ID = CreateFullID(Azriel.class.getSimpleName());

    public Azriel()
    {
        super(ID, 3, CardType.POWER, CardTarget.SELF);

        Initialize(0,0, 1);

        SetSynergy(Synergies.NoGameNoLife);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper.ApplyPower(p, p, new PlayerFlightPower(p, 1), 1);
        GameActionsHelper.ApplyPower(p, p, new AzrielPower(p, this.magicNumber), this.magicNumber);
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeBaseCost(2);
        }
    }
}