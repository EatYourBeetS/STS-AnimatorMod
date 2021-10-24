package eatyourbeets.cards.animator.series.HitsugiNoChaika;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.utilities.GameActions;

public class Guy extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Guy.class)
            .SetSkill(0, CardRarity.COMMON, EYBCardTarget.None)
            .SetSeriesFromClassPackage();

    public Guy()
    {
        super(DATA);

        Initialize(0, 0, 2, 0);
        SetUpgrade(0, 0, 1);

        SetAffinity_Mind();
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.Draw(magicNumber);
        GameActions.Bottom.DiscardFromHand(name, magicNumber, false)
        .SetOptions(false, false, true);
    }
}