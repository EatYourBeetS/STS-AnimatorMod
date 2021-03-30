package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Frost;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.powers.animator.OrbCore_FrostPower;
import eatyourbeets.utilities.GameActions;

public class OrbCore_Frost extends OrbCore
{
    public static final EYBCardData DATA = Register(OrbCore_Frost.class).SetPower(0, CardRarity.SPECIAL).SetColor(CardColor.COLORLESS);

    public static final int VALUE = 2;

    public OrbCore_Frost()
    {
        super(DATA);

        Initialize(0, 0, VALUE, 2);

        SetEvokeOrbCount(secondaryValue);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.ChannelOrbs(Frost::new, secondaryValue);
        GameActions.Bottom.StackPower(new OrbCore_FrostPower(p, 1));
    }
}