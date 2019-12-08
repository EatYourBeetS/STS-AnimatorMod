package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Frost;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.utilities.GameActionsHelper; import eatyourbeets.utilities.GameActionsHelper2;
import eatyourbeets.powers.animator.OrbCore_FrostPower;
import eatyourbeets.utilities.GameActionsHelper2;

public class OrbCore_Frost extends OrbCore
{
    public static final String ID = Register(OrbCore_Frost.class.getSimpleName(), EYBCardBadge.Special);

    public static final int VALUE = 2;

    public OrbCore_Frost()
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
            GameActionsHelper.ChannelOrb(new Frost(), true);
        }

        GameActionsHelper2.StackPower(new OrbCore_FrostPower(p, 1));
    }
}