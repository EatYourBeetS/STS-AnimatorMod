package eatyourbeets.cards.animator.status;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.series.Konosuba.Megumin;
import eatyourbeets.cards.base.AnimatorCard_Status;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;

public class Konosuba_Slimed extends AnimatorCard_Status
{
    public static final EYBCardData DATA = Register(Konosuba_Slimed.class)
            .SetStatus(1, CardRarity.COMMON, EYBCardTarget.None)
            .SetSeries(Megumin.DATA.Series);

    public Konosuba_Slimed()
    {
        super(DATA, false);

        Initialize(0, 0);

        SetExhaust(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {

    }
}