package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.utilities.GameActionsHelper; import eatyourbeets.utilities.GameActionsHelper2;
import eatyourbeets.orbs.Earth;
import eatyourbeets.powers.animator.OrbCore_EarthPower;
import eatyourbeets.utilities.GameActionsHelper2;

public class OrbCore_Earth extends OrbCore
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
            GameActionsHelper.ChannelOrb(new Earth(), true);
        }

        GameActionsHelper2.StackPower(new OrbCore_EarthPower(p, 1));
    }
}