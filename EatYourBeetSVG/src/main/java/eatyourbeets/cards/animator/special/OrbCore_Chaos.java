package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCardBadge;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.powers.animator.OrbCore_ChaosPower;

public class OrbCore_Chaos extends OrbCore
{
    public static final String ID = Register(OrbCore_Chaos.class.getSimpleName(), EYBCardBadge.Special);

    public static final int VALUE = 1;

    public OrbCore_Chaos()
    {
        super(ID, 1);

        Initialize(0, 0, VALUE, 1);

        SetEvokeOrbCount(secondaryValue);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        for (int i = 0; i < secondaryValue; i++)
        {
            GameActions.Bottom.ChannelRandomOrb(true);
        }

        GameActions.Bottom.StackPower(new OrbCore_ChaosPower(p, 1));
    }
}