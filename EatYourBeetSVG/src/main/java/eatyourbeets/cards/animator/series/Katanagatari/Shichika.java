package eatyourbeets.cards.animator.series.Katanagatari;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.special.ShichikaKyotouryuu;
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
        DATA.AddPreview(new ShichikaKyotouryuu(), false);
    }

    public Shichika()
    {
        super(DATA);

        Initialize(0, 2);
        SetUpgrade(0, 0);

        SetAffinity_Red(2, 0, 1);
        SetAffinity_Green(2);

        SetExhaust(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.GainBlock(block).SetVFX(false, true);
        GameActions.Bottom.GainForce(1, upgraded);
        GameActions.Bottom.GainAgility(1, upgraded);
        GameActions.Bottom.MakeCardInHand(new ShichikaKyotouryuu());
    }
}