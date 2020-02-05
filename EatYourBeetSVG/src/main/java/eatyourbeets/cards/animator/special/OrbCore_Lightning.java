package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Lightning;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.powers.animator.OrbCore_LightningPower;
import eatyourbeets.utilities.GameActions;

public class OrbCore_Lightning extends OrbCore
{
    public static final EYBCardData DATA = Register(OrbCore_Lightning.class).SetPower(0, CardRarity.SPECIAL).SetColor(CardColor.COLORLESS);

    public static final int VALUE = 9;

    public OrbCore_Lightning()
    {
        super(DATA);

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