package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Plasma;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.powers.animator.OrbCore_PlasmaPower;
import eatyourbeets.utilities.GameActions;

public class OrbCore_Plasma extends OrbCore
{
    public static final EYBCardData DATA = Register(OrbCore_Plasma.class).SetPower(1, CardRarity.SPECIAL).SetColor(CardColor.COLORLESS);

    public static final int VALUE = 3;

    public OrbCore_Plasma()
    {
        super(DATA);

        Initialize(0, 0, VALUE, 1);

        SetEvokeOrbCount(secondaryValue);
        SetAffinity_Light(2);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.ChannelOrbs(Plasma::new, secondaryValue);
        GameActions.Bottom.StackPower(new OrbCore_PlasmaPower(p, 1));
    }
}