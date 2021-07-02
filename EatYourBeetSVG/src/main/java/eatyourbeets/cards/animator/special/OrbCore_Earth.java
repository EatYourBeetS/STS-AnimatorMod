package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.orbs.animator.Earth;
import eatyourbeets.powers.animator.OrbCore_EarthPower;
import eatyourbeets.utilities.GameActions;

public class OrbCore_Earth extends OrbCore
{
    public static final EYBCardData DATA = Register(OrbCore_Earth.class).SetPower(1, CardRarity.SPECIAL).SetColor(CardColor.COLORLESS);

    public static final int VALUE = 5;

    public OrbCore_Earth()
    {
        super(DATA);

        Initialize(0, 0, VALUE, 1);

        SetEvokeOrbCount(secondaryValue);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.ChannelOrbs(Earth::new, secondaryValue);
        GameActions.Bottom.StackPower(new OrbCore_EarthPower(p, 1));
    }
}