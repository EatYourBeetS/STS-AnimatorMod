package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Dark;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.powers.animator.OrbCore_DarkPower;
import eatyourbeets.utilities.GameActions;

public class OrbCore_Dark extends OrbCore
{
    public static final EYBCardData DATA = Register(OrbCore_Dark.class).SetPower(0, CardRarity.SPECIAL).SetColor(CardColor.COLORLESS);

    public static final int VALUE = 1;

    public OrbCore_Dark()
    {
        super(DATA);

        Initialize(0, 0, VALUE, 1);

        SetEvokeOrbCount(secondaryValue);
        SetAffinity_Dark(2);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.ChannelOrbs(Dark::new, secondaryValue);
        GameActions.Bottom.StackPower(new OrbCore_DarkPower(p, 1));
    }
}