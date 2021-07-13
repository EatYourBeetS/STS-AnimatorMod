package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.orbs.animator.Fire;
import eatyourbeets.powers.animator.OrbCore_FirePower;
import eatyourbeets.utilities.GameActions;

public class OrbCore_Fire extends OrbCore
{
    public static final EYBCardData DATA = Register(OrbCore_Fire.class)
            .SetPower(0, CardRarity.SPECIAL)
            .SetColor(CardColor.COLORLESS);

    public static final int VALUE = 3;

    public OrbCore_Fire()
    {
        super(DATA);

        Initialize(0, 0, VALUE, 2);

        SetAffinity_Red(2);

        SetEvokeOrbCount(secondaryValue);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.ChannelOrbs(Fire::new, secondaryValue);
        GameActions.Bottom.StackPower(new OrbCore_FirePower(p, 1));
    }
}