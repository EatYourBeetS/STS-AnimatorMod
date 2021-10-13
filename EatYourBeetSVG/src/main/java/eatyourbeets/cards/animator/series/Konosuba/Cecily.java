package eatyourbeets.cards.animator.series.Konosuba;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.utilities.ColoredString;
import eatyourbeets.utilities.Colors;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Cecily extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Cecily.class)
            .SetSkill(0, CardRarity.UNCOMMON, EYBCardTarget.None)
            .SetSeriesFromClassPackage();

    private ColoredString magicNumberString = new ColoredString("X", Colors.Cream(1));

    public Cecily()
    {
        super(DATA);

        Initialize(0, 0, 0, 0);
        SetUpgrade(0,0,0);

        SetAffinity_Light();

        SetExhaust(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetRetain(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.MoveCards(p.hand, p.discardPile)
        .AddCallback( cards ->
        {
            int numCardsDraw = cards.size();

            if (numCardsDraw > 0)
                GameActions.Bottom.Draw(numCardsDraw).SetFilter(GameUtilities::IsLowCost, false);
        });
    }
}