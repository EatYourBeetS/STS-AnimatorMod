package eatyourbeets.cards.animatorClassic.beta;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorClassicCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.interfaces.markers.Hidden;

public class ManiwaHouou extends AnimatorClassicCard implements Hidden
{
    public static final EYBCardData DATA = Register(ManiwaHouou.class).SetAttack(2, CardRarity.COMMON);

    public ManiwaHouou()
    {
        super(DATA);

        Initialize(0, 0);
        SetUpgrade(0, 0);

        SetEthereal(true);
        this.series = CardSeries.Katanagatari;
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {

    }
}