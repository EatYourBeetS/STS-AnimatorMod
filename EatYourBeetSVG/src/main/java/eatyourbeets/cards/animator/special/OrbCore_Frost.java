package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Frost;
import eatyourbeets.cards.base.EYBCardBadge;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.powers.animator.OrbCore_FrostPower;

public class OrbCore_Frost extends OrbCore
{
    public static final String ID = Register(OrbCore_Frost.class, EYBCardBadge.Special);

    public static final int VALUE = 2;

    public OrbCore_Frost()
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
            GameActions.Bottom.ChannelOrb(new Frost(), true);
        }

        GameActions.Bottom.StackPower(new OrbCore_FrostPower(p, 1));
    }
}