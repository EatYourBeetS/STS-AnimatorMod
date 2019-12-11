package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Plasma;
import eatyourbeets.cards.base.EYBCardBadge;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.powers.animator.OrbCore_PlasmaPower;

public class OrbCore_Plasma extends OrbCore
{
    public static final String ID = Register(OrbCore_Plasma.class.getSimpleName(), EYBCardBadge.Special);

    public static final int VALUE = 3;

    public OrbCore_Plasma()
    {
        super(ID, 1);

        Initialize(0,0, VALUE,1);

        SetEvokeOrbCount(secondaryValue);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        for (int i = 0; i < secondaryValue; i++)
        {
            GameActions.Bottom.ChannelOrb(new Plasma(), true);
        }

        GameActions.Bottom.StackPower(new OrbCore_PlasmaPower(p, 1));
    }
}