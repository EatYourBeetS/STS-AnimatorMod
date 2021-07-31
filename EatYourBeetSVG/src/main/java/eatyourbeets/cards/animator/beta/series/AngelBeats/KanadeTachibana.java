package eatyourbeets.cards.animator.beta.series.AngelBeats;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.utilities.GameActions;

public class KanadeTachibana extends AnimatorCard
{
    public static final EYBCardData DATA = Register(KanadeTachibana.class).SetSkill(1, CardRarity.RARE, EYBCardTarget.None).SetSeriesFromClassPackage();

    public KanadeTachibana()
    {
        super(DATA);

        Initialize(0, 0, 3, 4);
        SetUpgrade(0, 0, 1, 1);

        SetExhaust(true);
        SetAffinity_Blue(2, 0, 0);
        SetAffinity_Light(2, 0, 0);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Top.FetchFromPile(name, magicNumber, p.discardPile)
        .SetOptions(false, true)
        .SetMessage(cardData.Strings.EXTENDED_DESCRIPTION[0]);

        if (HasSynergy())
        {
            GameActions.Bottom.ExhaustFromHand(name, secondaryValue, false)
            .SetOptions(true, true, true);
        }
    }
}