package eatyourbeets.cards.animator.beta;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.interfaces.markers.Hidden;

public class ManiwaHouou extends AnimatorCard implements Hidden
{
    public static final EYBCardData DATA = Register(ManiwaHouou.class)
            .SetAttack(2, CardRarity.COMMON)
            .SetSeries(CardSeries.Katanagatari);

    public ManiwaHouou()
    {
        super(DATA);

        Initialize(0, 0);
        SetUpgrade(0, 0);

        SetEthereal(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {

    }
}