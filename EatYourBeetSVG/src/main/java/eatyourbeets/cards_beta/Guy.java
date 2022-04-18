package eatyourbeets.cards_beta;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.interfaces.markers.Hidden;
import eatyourbeets.utilities.CardSelection;
import eatyourbeets.utilities.GameActions;

public class Guy extends AnimatorCard implements Hidden
{
    public static final EYBCardData DATA = Register(Guy.class)
            .SetSkill(0, CardRarity.COMMON, EYBCardTarget.None)
            .SetSeries(CardSeries.HitsugiNoChaika);

    public Guy()
    {
        super(DATA);

        Initialize(0, 0, 1, 2);
        SetUpgrade(0, 0, 1);

        SetAffinity_Dark(1);
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