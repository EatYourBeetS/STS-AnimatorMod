package eatyourbeets.cards.animator.series.Fate;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.powers.animator.ArcherPower;
import eatyourbeets.utilities.GameActions;

public class Archer extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Archer.class)
            .SetPower(1, CardRarity.UNCOMMON)
            .SetSeriesFromClassPackage();

    public Archer()
    {
        super(DATA);

        Initialize(0, 0, 3);
        SetUpgrade(0, 0, 3);

        SetAffinity_Poison();
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.StackPower(new ArcherPower(p, magicNumber));
    }
}