package eatyourbeets.cards.animator.ultrarare;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard_UltraRare;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.animator.AzrielPower;
import eatyourbeets.powers.common.PlayerFlightPower;
import eatyourbeets.utilities.GameActions;

public class Azriel extends AnimatorCard_UltraRare
{
    public static final EYBCardData DATA = Register(Azriel.class).SetPower(3, CardRarity.SPECIAL).SetColor(CardColor.COLORLESS);

    public Azriel()
    {
        super(DATA);

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
        if (!p.hasPower(PlayerFlightPower.POWER_ID))
        {
            GameActions.Bottom.StackPower(new PlayerFlightPower(p, 2));
        }

        GameActions.Bottom.StackPower(new AzrielPower(p, this.magicNumber));
    }
}