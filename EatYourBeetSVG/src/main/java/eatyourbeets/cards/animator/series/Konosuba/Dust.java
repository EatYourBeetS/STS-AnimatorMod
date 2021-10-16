package eatyourbeets.cards.animator.series.Konosuba;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.utilities.GameActions;

public class Dust extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Dust.class)
            .SetAttack(1, CardRarity.COMMON)
            .SetSeriesFromClassPackage();

    public Dust()
    {
        super(DATA);

        Initialize(9, 0, 1);
        SetUpgrade(4, 0, 0);

        SetAffinity_Fire();

        SetAffinityRequirement(Affinity.Fire, 6);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (CheckAffinity(Affinity.Fire)) {
            GameActions.Bottom.FetchFromPile(name, magicNumber, p.drawPile);
        };
    }
}