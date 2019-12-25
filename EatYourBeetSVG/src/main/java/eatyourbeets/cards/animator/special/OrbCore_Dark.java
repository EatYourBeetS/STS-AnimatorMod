package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Dark;
import eatyourbeets.cards.base.EYBCardBadge;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.powers.animator.OrbCore_DarkPower;

public class OrbCore_Dark extends OrbCore
{
    public static final String ID = Register(OrbCore_Dark.class.getSimpleName(), EYBCardBadge.Special);

    public static final int VALUE = 1;

    public OrbCore_Dark()
    {
        super(ID, 0);

        Initialize(0, 0, VALUE, 1);

        SetEvokeOrbCount(secondaryValue);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        for (int i = 0; i < secondaryValue; i++)
        {
            GameActions.Bottom.ChannelOrb(new Dark(), true);
        }

        GameActions.Bottom.StackPower(new OrbCore_DarkPower(p, 1));
    }
}