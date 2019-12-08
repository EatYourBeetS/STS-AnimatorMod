package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.utilities.GameActionsHelper; import eatyourbeets.utilities.GameActionsHelper2;
import eatyourbeets.orbs.Fire;
import eatyourbeets.powers.animator.OrbCore_FirePower;
import eatyourbeets.utilities.GameActionsHelper2;

public class OrbCore_Fire extends OrbCore
{
    public static final String ID = Register(OrbCore_Fire.class.getSimpleName(), EYBCardBadge.Special);

    public static final int VALUE = 3;

    public OrbCore_Fire()
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
            GameActionsHelper.ChannelOrb(new Fire(), true);
        }

        GameActionsHelper2.StackPower(new OrbCore_FirePower(p, 1));
    }
}