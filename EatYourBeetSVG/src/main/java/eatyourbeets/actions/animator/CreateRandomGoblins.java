package eatyourbeets.actions.animator;

import com.megacrit.cardcrawl.random.Random;
import eatyourbeets.actions.EYBAction;
import eatyourbeets.cards.animator.status.GoblinChampion;
import eatyourbeets.cards.animator.status.GoblinKing;
import eatyourbeets.cards.animator.status.GoblinShaman;
import eatyourbeets.cards.animator.status.GoblinSoldier;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.utilities.GameActions;

public class CreateRandomGoblins extends EYBAction
{
    public CreateRandomGoblins(int amount)
    {
        super(ActionType.CARD_MANIPULATION);

        Initialize(amount);
    }

    @Override
    protected void FirstUpdate()
    {
        for (int i = 0; i < amount; i++)
        {
            GameActions.Bottom.MakeCardInDrawPile(GetRandomGoblin(rng));
        }

        Complete();
    }

    public static AnimatorCard GetRandomGoblin(Random rng)
    {
        int n = rng.random(100);
        if (n < 35) // 35%
        {
            return new GoblinSoldier();
        }
        else if (n < 65) // 30%
        {
            return new GoblinShaman();
        }
        else if (n < 85) // 20%
        {
            return new GoblinChampion();
        }
        else // 15%
        {
            return new GoblinKing();
        }
    }
}
