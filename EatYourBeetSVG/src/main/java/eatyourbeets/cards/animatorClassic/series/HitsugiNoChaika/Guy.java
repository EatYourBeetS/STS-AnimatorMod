package eatyourbeets.cards.animatorClassic.series.HitsugiNoChaika;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorClassicCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.utilities.CardSelection;
import eatyourbeets.utilities.GameActions;

public class Guy extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(Guy.class).SetSkill(0, CardRarity.COMMON, EYBCardTarget.None);

    public Guy()
    {
        super(DATA);

        Initialize(0, 0, 1, 2);
        SetUpgrade(0, 0, 1);

        SetSeries(CardSeries.HitsugiNoChaika);
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.Draw(magicNumber);
        GameActions.Bottom.DiscardFromHand(name, magicNumber, false)
        .SetOptions(false, false, true);

        if (info.IsSynergizing)
        {
            GameActions.Bottom.MoveCards(p.drawPile, p.discardPile, secondaryValue)
            .ShowEffect(true, true)
            .SetOrigin(CardSelection.Top);
        }
    }
}