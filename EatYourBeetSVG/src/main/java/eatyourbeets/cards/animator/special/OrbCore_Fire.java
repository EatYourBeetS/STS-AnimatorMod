package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCardBadge;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.orbs.Fire;
import eatyourbeets.powers.animator.OrbCore_FirePower;

public class OrbCore_Fire extends OrbCore
{
    public static final String ID = Register(OrbCore_Fire.class, EYBCardBadge.Special);

    public static final int VALUE = 3;

    public OrbCore_Fire()
    {
        super(ID, 0);

        Initialize(0, 0, VALUE, 2);

        SetEvokeOrbCount(secondaryValue);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        for (int i = 0; i < secondaryValue; i++)
        {
            GameActions.Bottom.ChannelOrb(new Fire(), true);
        }

        GameActions.Bottom.StackPower(new OrbCore_FirePower(p, 1));
    }
}