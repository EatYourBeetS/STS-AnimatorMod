package eatyourbeets.blights.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.actions.cardManipulation.GenerateCard;
import eatyourbeets.blights.AnimatorBlight;
import eatyourbeets.cards.animator.status.Status_Burn;
import eatyourbeets.cards.animator.status.Status_Void;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.interfaces.subscribers.OnBattleStartSubscriber;
import eatyourbeets.interfaces.subscribers.OnShuffleSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JUtils;

public class UltimateWispBlight extends AnimatorBlight implements OnBattleStartSubscriber, OnShuffleSubscriber
{
    public static final String ID = CreateFullID(UltimateWispBlight.class);

    protected EYBCardData status;

    public UltimateWispBlight()
    {
        super(ID, 1);

        this.counter = -1;

        UpdateCardData();
    }

    @Override
    public String GetUpdatedDescription()
    {
        return FormatDescription(0, initialAmount, status == null ? "Status card" : JUtils.ModifyString(status.Strings.NAME, w -> "#y" + w));
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
            final GenerateCard temp = JUtils.SafeCast(action, GenerateCard.class);
            if (temp != null && temp.HasTag(this))
            {
                return;
            }
        }

        GameActions.Bottom.MakeCardInDrawPile(status.MakeCopy(false)).AddTag(this);

        this.flash();
    }

    @Override
    public void OnBattleStart()
    {
        UpdateCardData();
        CombatStats.onBattleStart.Subscribe(this);
        CombatStats.onShuffle.Subscribe(this);
    }

    protected void UpdateCardData()
    {
        final EYBCardData temp = IsUnnamedReign() ? Status_Void.DATA : Status_Burn.DATA;
        if (temp != status)
        {
            status = temp;
            updateDescription();
        }
    }
}