package eatyourbeets.cards.animator.status;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.series.GoblinSlayer.GoblinSlayer;
import eatyourbeets.cards.base.AnimatorCard_Status;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.utilities.GameActions;

public class GoblinShaman extends AnimatorCard_Status
{
    public static final EYBCardData DATA = Register(GoblinShaman.class)
            .SetStatus(1, CardRarity.COMMON, EYBCardTarget.None)
            .SetSeries(GoblinSlayer.DATA.Series);

    public GoblinShaman()
    {
        super(DATA, true);

        Initialize(0, 0);

        SetAffinity_Blue(1);
        SetAffinity_Dark(1);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        GameActions.Bottom.Draw(1);
        GameActions.Bottom.Flash(this);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        if (this.dontTriggerOnUseCard)
        {
            GameActions.Bottom.ApplyFrail(null, p, 1);
        }
    }
}