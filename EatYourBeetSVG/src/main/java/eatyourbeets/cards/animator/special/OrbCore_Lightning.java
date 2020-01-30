package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Lightning;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.powers.animator.OrbCore_LightningPower;

public class OrbCore_Lightning extends OrbCore
{
    public static final String ID = Register(OrbCore_Lightning.class);

    public static final int VALUE = 9;

    public OrbCore_Lightning()
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
            GameActions.Bottom.ChannelOrb(new Lightning(), true);
        }

        GameActions.Bottom.StackPower(new OrbCore_LightningPower(p, 1));
    }
}