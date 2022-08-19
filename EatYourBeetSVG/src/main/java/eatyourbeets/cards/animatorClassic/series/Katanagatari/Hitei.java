package eatyourbeets.cards.animatorClassic.series.Katanagatari;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorClassicCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.powers.animatorClassic.HiteiPower;
import eatyourbeets.utilities.GameActions;

public class Hitei extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(Hitei.class).SetPower(1, CardRarity.UNCOMMON);

    public Hitei()
    {
        super(DATA);

        Initialize(0, 0, 0, 2);
        SetUpgrade(0, 0, 0, 1);

        SetSeries(CardSeries.Katanagatari);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.StackPower(new HiteiPower(p, upgraded));
    }
}