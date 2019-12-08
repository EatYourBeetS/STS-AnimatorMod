package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Lightning;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.utilities.GameActionsHelper; import eatyourbeets.utilities.GameActionsHelper2;
import eatyourbeets.powers.animator.OrbCore_LightningPower;
import eatyourbeets.utilities.GameActionsHelper2;

public class OrbCore_Lightning extends OrbCore
{
    public static final String ID = Register(OrbCore_Lightning.class.getSimpleName(), EYBCardBadge.Special);

    public static final int VALUE = 9;

    public OrbCore_Lightning()
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
            GameActionsHelper.ChannelOrb(new Lightning(), true);
        }

        GameActionsHelper2.StackPower(new OrbCore_LightningPower(p, 1));
    }
}