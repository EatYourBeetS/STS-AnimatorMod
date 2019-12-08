package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.utilities.GameActionsHelper; import eatyourbeets.utilities.GameActionsHelper2;
import eatyourbeets.powers.animator.OrbCore_ChaosPower;
import eatyourbeets.utilities.GameActionsHelper2;

public class OrbCore_Chaos extends OrbCore
{
    public static final String ID = Register(OrbCore_Chaos.class.getSimpleName(), EYBCardBadge.Special);

    public static final int VALUE = 1;

    public OrbCore_Chaos()
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
            GameActionsHelper.ChannelRandomOrb(true);
        }

        GameActionsHelper2.StackPower(new OrbCore_ChaosPower(p, 1));
    }
}