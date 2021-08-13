package eatyourbeets.blights.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.status.VoidCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.actions.cardManipulation.GenerateCard;
import eatyourbeets.blights.AnimatorBlight;
import eatyourbeets.interfaces.subscribers.OnBattleStartSubscriber;
import eatyourbeets.interfaces.subscribers.OnShuffleSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JUtils;

public class UltimateWispBlight extends AnimatorBlight implements OnBattleStartSubscriber, OnShuffleSubscriber
{
    public static final String ID = CreateFullID(UltimateWispBlight.class);

    public UltimateWispBlight()
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
            GenerateCard temp = JUtils.SafeCast(action, GenerateCard.class);
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
        CombatStats.onBattleStart.Subscribe(this);
        CombatStats.onShuffle.Subscribe(this);
    }
}