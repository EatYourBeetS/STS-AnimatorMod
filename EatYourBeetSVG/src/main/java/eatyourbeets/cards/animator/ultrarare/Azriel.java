package eatyourbeets.cards.animator.ultrarare;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard_UltraRare;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.powers.animator.AzrielPower;
import eatyourbeets.powers.replacement.PlayerFlightPower;
import eatyourbeets.utilities.GameActions;

public class Azriel extends AnimatorCard_UltraRare
{
    public static final EYBCardData DATA = Register(Azriel.class)
            .SetPower(3, CardRarity.SPECIAL)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.NoGameNoLife);

    public Azriel()
    {
        super(DATA);

        Initialize(0, 0, 1);

        SetAffinity_Red(1);
        SetAffinity_Blue(1);
        SetAffinity_Dark(1);

        SetEthereal(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetEthereal(false);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (!p.hasPower(PlayerFlightPower.POWER_ID))
        {
            GameActions.Bottom.StackPower(new PlayerFlightPower(p, 2));
        }

        GameActions.Bottom.StackPower(new AzrielPower(p, magicNumber));
    }
}