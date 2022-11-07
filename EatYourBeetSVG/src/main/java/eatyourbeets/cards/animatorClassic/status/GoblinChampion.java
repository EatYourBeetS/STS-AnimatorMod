package eatyourbeets.cards.animatorClassic.status;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.animator.CreateRandomGoblins;
import eatyourbeets.cards.base.*;
import eatyourbeets.utilities.GameActions;

public class GoblinChampion extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(GoblinChampion.class).SetStatus(1, CardRarity.UNCOMMON, EYBCardTarget.None);

    public GoblinChampion()
    {
        super(DATA);

        Initialize(0, 0);

        SetEndOfTurnPlay(true);
        this.series = CardSeries.GoblinSlayer;
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