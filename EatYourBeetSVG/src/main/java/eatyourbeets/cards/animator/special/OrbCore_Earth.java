package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCardBadge;
import eatyourbeets.interfaces.markers.Hidden;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.orbs.Earth;
import eatyourbeets.powers.animator.OrbCore_EarthPower;

public class OrbCore_Earth extends OrbCore implements Hidden
{
    public static final String ID = Register(OrbCore_Earth.class.getSimpleName(), EYBCardBadge.Special);

    public static final int VALUE = 3;

    public OrbCore_Earth()
    {
        super(ID, 0);

        Initialize(0,0, VALUE,2);

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