package eatyourbeets.cards.animator.series.OnePunchMan;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.utilities.CardSelection;
import eatyourbeets.utilities.GameActions;

public class Garou extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Garou.class)
            .SetSkill(1, CardRarity.RARE, EYBCardTarget.None)
            .SetSeriesFromClassPackage()
            .ObtainableAsReward((data, deck) -> deck.size() >= (14 + (7 * data.GetTotalCopies(deck))));

    public Garou()
    {
        super(DATA);

        Initialize(0, 0, 7, 3);

        SetAffinity_Red(2);
        SetAffinity_Green(1);
        SetAffinity_Dark(1, 1, 0);

        SetExhaust(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetExhaust(false);
    }

    @Override
    protected void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        SetUnplayable(player.drawPile.size() < secondaryValue);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (p.drawPile.size() >= secondaryValue)
        {
            GameActions.Bottom.GainForce(1, true);
            GameActions.Bottom.GainAgility(1, true);
            GameActions.Bottom.GainCorruption(1, true);
            GameActions.Bottom.GainTemporaryStats(magicNumber, magicNumber, 0);
            GameActions.Bottom.MoveCards(p.drawPile, p.exhaustPile, 3)
            .ShowEffect(true, true)
            .SetOrigin(CardSelection.Top);
        }
    }
}