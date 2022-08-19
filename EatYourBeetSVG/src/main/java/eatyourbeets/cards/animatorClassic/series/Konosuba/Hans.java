package eatyourbeets.cards.animatorClassic.series.Konosuba;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorClassicCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.powers.animatorClassic.HansPower;
import eatyourbeets.utilities.GameActions;

public class Hans extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(Hans.class).SetPower(3, CardRarity.RARE);

    public Hans()
    {
        super(DATA);

        Initialize(0, 0, 3, 1);
        SetUpgrade(0, 0, 1, 0);

        SetShapeshifter();
        SetSeries(CardSeries.Konosuba);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.StackPower(new HansPower(p, magicNumber, secondaryValue));
    }
}