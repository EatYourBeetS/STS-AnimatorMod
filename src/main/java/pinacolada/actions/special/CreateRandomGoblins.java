package pinacolada.actions.special;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.random.Random;
import eatyourbeets.actions.EYBActionWithCallback;
import eatyourbeets.interfaces.delegates.ActionT1;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.pcl.status.GoblinChampion;
import pinacolada.cards.pcl.status.GoblinKing;
import pinacolada.cards.pcl.status.GoblinShaman;
import pinacolada.cards.pcl.status.GoblinSoldier;
import pinacolada.utilities.PCLActions;

public class CreateRandomGoblins extends EYBActionWithCallback<AbstractCard>
{
    public CreateRandomGoblins(int amount)
    {
        super(ActionType.CARD_MANIPULATION);

        Initialize(amount);
    }

    @Override
    protected void FirstUpdate()
    {
        final float speed = amount < 2 ? Settings.ACTION_DUR_FAST : amount < 3 ? Settings.ACTION_DUR_FASTER : Settings.ACTION_DUR_XFAST;
        for (int i = 0; i < amount; i++)
        {
            PCLActions.Top.MakeCardInDrawPile(GetRandomGoblin(rng))
            .AddCallback((ActionT1<AbstractCard>) this::Complete)
            .SetDuration(speed, true);
        }

        Complete();
    }

    public static PCLCard GetRandomGoblin(Random rng)
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
