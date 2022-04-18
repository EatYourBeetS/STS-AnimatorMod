package eatyourbeets.cards.animator.status;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.series.GoblinSlayer.GoblinSlayer;
import eatyourbeets.cards.base.*;
import eatyourbeets.utilities.GameActions;

public class GoblinShaman extends AnimatorCard
{
    public static final EYBCardData DATA = Register(GoblinShaman.class)
            .SetStatus(1, CardRarity.COMMON, EYBCardTarget.None)
            .SetSeries(GoblinSlayer.DATA.Series);

    public GoblinShaman()
    {
        super(DATA);

        Initialize(0, 0);

        SetAffinity_Blue(1);
        SetAffinity_Dark(1);

        SetEndOfTurnPlay(true);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        GameActions.Bottom.Draw(1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (this.dontTriggerOnUseCard)
        {
            GameActions.Bottom.ApplyFrail(null, p, 1);
            GameActions.Bottom.GainCorruption(1);
        }
    }
}