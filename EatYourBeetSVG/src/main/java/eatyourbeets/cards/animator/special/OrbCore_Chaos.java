package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.powers.animator.OrbCore_ChaosPower;
import eatyourbeets.utilities.GameActions;

public class OrbCore_Chaos extends OrbCore
{
    public static final EYBCardData DATA = Register(OrbCore_Chaos.class).SetPower(1, CardRarity.SPECIAL).SetColor(CardColor.COLORLESS);

    public static final int VALUE = 1;

    public OrbCore_Chaos()
    {
        super(DATA);

        Initialize(0, 0, VALUE, 1);

        SetEvokeOrbCount(secondaryValue);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.ChannelRandomOrbs(secondaryValue);
        GameActions.Bottom.StackPower(new OrbCore_ChaosPower(p, 1));
    }
}