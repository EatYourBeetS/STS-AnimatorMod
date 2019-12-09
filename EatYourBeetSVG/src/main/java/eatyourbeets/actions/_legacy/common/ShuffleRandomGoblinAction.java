package eatyourbeets.actions._legacy.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.random.Random;
import eatyourbeets.utilities.GameActionsHelper_Legacy;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.animator.GoblinChampion;
import eatyourbeets.cards.animator.GoblinKing;
import eatyourbeets.cards.animator.GoblinShaman;
import eatyourbeets.cards.animator.GoblinSoldier;

public class ShuffleRandomGoblinAction extends AbstractGameAction
{
    private final int count;

    public ShuffleRandomGoblinAction(int count)
    {
        this.count = count;
        this.target = AbstractDungeon.player;
        this.duration = Settings.ACTION_DUR_FAST;
        this.actionType = ActionType.SHUFFLE;
    }

    public void update()
    {
        if (this.duration == Settings.ACTION_DUR_FAST)
        {
            for (int i = 0; i < count; i++)
            {
                GameActionsHelper_Legacy.AddToBottom(new MakeTempCardInDrawPileAction(GetRandomGoblin(AbstractDungeon.cardRandomRng), 1, true, true, false));
            }
        }

        this.tickDuration();
    }

    private AnimatorCard GetRandomGoblin(Random rng)
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
