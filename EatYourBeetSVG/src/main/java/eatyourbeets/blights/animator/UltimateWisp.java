package eatyourbeets.blights.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.status.VoidCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.actions.cardManipulation.MakeTempCard;
import eatyourbeets.blights.AnimatorBlight;
import eatyourbeets.interfaces.subscribers.OnBattleStartSubscriber;
import eatyourbeets.interfaces.subscribers.OnShuffleSubscriber;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JavaUtilities;

public class UltimateWisp extends AnimatorBlight implements OnBattleStartSubscriber, OnShuffleSubscriber
{
    public static final String ID = CreateFullID(UltimateWisp.class);

    public UltimateWisp()
    {
        super(ID, 1);

        this.counter = -1;
    }

    @Override
    public void atBattleStart()
    {
        super.atBattleStart();

        OnBattleStart();
    }

    @Override
    public void OnShuffle(boolean triggerRelics)
    {
        for (AbstractGameAction action : AbstractDungeon.actionManager.actions)
        {
            MakeTempCard temp = JavaUtilities.SafeCast(action, MakeTempCard.class);
            if (temp != null && temp.HasTag(this))
            {
                return;
            }
        }

        GameActions.Bottom.MakeCardInDrawPile(new VoidCard()).AddTag(this);

        this.flash();
    }

    @Override
    public void OnBattleStart()
    {
        PlayerStatistics.onBattleStart.Subscribe(this);
        PlayerStatistics.onShuffle.Subscribe(this);
    }
}