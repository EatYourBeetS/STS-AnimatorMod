package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.interfaces.markers.Hidden;
import eatyourbeets.orbs.animator.Earth;
import eatyourbeets.powers.animator.OrbCore_EarthPower;
import eatyourbeets.utilities.GameActions;

public class OrbCore_Earth extends OrbCore implements Hidden
{
    public static final EYBCardData DATA = Register(OrbCore_Earth.class).SetPower(0, CardRarity.SPECIAL).SetColor(CardColor.COLORLESS);

    public static final int VALUE = 3;

    public OrbCore_Earth()
    {
        super(DATA);

        Initialize(0, 0, VALUE, 2);

        SetEvokeOrbCount(secondaryValue);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        for (int i = 0; i < secondaryValue; i++)
        {
            GameActions.Bottom.ChannelOrb(new Earth(), true);
        }

        GameActions.Bottom.StackPower(new OrbCore_EarthPower(p, 1));
    }
}