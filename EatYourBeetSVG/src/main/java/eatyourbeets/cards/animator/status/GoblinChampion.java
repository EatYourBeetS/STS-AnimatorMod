package eatyourbeets.cards.animator.status;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.animator.CreateRandomGoblins;
import eatyourbeets.cards.animator.series.GoblinSlayer.GoblinSlayer;
import eatyourbeets.cards.base.*;
import eatyourbeets.utilities.GameActions;

public class GoblinChampion extends AnimatorCard
{
    public static final EYBCardData DATA = Register(GoblinChampion.class)
            .SetStatus(1, CardRarity.UNCOMMON, EYBCardTarget.None)
            .SetSeries(GoblinSlayer.DATA.Series);

    public GoblinChampion()
    {
        super(DATA);

        Initialize(0, 0);

        SetEndOfTurnPlay(true);

        SetAffinity_Red(1);
        SetAffinity_Dark(1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (this.dontTriggerOnUseCard)
        {
            GameActions.Bottom.Add(new CreateRandomGoblins(1));
        }
        else
        {
            GameActions.Bottom.GainAffinity(Affinity.Red, 1, false);
        }
    }
}