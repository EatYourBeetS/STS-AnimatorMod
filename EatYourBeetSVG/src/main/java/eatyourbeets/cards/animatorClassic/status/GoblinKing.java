package eatyourbeets.cards.animatorClassic.status;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.animator.CreateRandomGoblins;
import eatyourbeets.cards.base.*;
import eatyourbeets.utilities.GameActions;

public class GoblinKing extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(GoblinKing.class).SetStatus(1, CardRarity.RARE, EYBCardTarget.None);

    public GoblinKing()
    {
        super(DATA);

        Initialize(0, 0);

        SetEndOfTurnPlay(true);
        SetSeries(CardSeries.GoblinSlayer);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (this.dontTriggerOnUseCard)
        {
            GameActions.Bottom.Add(new CreateRandomGoblins(3));
        }
    }
}