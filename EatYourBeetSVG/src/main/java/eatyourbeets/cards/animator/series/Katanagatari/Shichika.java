package eatyourbeets.cards.animator.series.Katanagatari;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.special.Shichika_Kyotouryuu;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.utilities.GameActions;

public class Shichika extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Shichika.class)
            .SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None)
            .SetMaxCopies(2)
            .SetSeriesFromClassPackage();
    static
    {
        DATA.AddPreview(new Shichika_Kyotouryuu(), false);
    }

    public Shichika()
    {
        super(DATA);

        Initialize(0, 2, 2);
        SetUpgrade(0, 0);

        SetAffinity_Red(2, 0, 1);
        SetAffinity_Green(2);

        SetExhaust(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.GainBlock(block).SetVFX(false, true);
        GameActions.Bottom.GainForce(magicNumber, upgraded);
        GameActions.Bottom.GainAgility(magicNumber, upgraded);
        GameActions.Bottom.MakeCardInHand(new Shichika_Kyotouryuu());
    }
}