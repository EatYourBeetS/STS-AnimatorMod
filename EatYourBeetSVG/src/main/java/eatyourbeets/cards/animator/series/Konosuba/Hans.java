package eatyourbeets.cards.animator.series.Konosuba;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.animator.HansPower;
import eatyourbeets.utilities.GameActions;

public class Hans extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Hans.class).SetPower(3, CardRarity.RARE);

    public Hans()
    {
        super(DATA);

        Initialize(0, 0, 3, 1);
        SetUpgrade(0, 0, 1, 0);

        SetSynergy(Synergies.Konosuba);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.StackPower(new HansPower(p, magicNumber, secondaryValue));
    }
}