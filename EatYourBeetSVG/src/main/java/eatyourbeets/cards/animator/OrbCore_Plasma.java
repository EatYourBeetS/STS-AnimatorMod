package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Plasma;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.utilities.GameActionsHelper; import eatyourbeets.utilities.GameActionsHelper2;
import eatyourbeets.powers.animator.OrbCore_PlasmaPower;
import eatyourbeets.utilities.GameActionsHelper2;

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
            GameActionsHelper.ChannelOrb(new Plasma(), true);
        }

        GameActionsHelper2.StackPower(new OrbCore_PlasmaPower(p, 1));
    }
}