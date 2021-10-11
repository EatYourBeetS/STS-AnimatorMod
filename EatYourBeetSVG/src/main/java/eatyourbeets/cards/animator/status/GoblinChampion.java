package eatyourbeets.cards.animator.status;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.animator.CreateRandomGoblins;
import eatyourbeets.cards.animator.series.GoblinSlayer.GoblinSlayer;
import eatyourbeets.cards.base.AnimatorCard_Status;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.utilities.GameActions;

public class GoblinChampion extends AnimatorCard_Status
{
    public static final EYBCardData DATA = Register(GoblinChampion.class)
            .SetStatus(1, CardRarity.UNCOMMON, EYBCardTarget.None)
            .SetSeries(GoblinSlayer.DATA.Series);

    public GoblinChampion()
    {
        super(DATA, true);

        Initialize(0, 0);

        SetAffinity_Fire(1);
        SetAffinity_Dark(1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (this.dontTriggerOnUseCard)
        {
            GameActions.Bottom.Add(new CreateRandomGoblins(1));
        }
    }
}