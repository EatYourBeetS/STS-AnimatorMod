package pinacolada.relics.pcl;

import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.rewards.RewardItem;
import eatyourbeets.interfaces.listeners.OnReceiveRewardsListener;
import eatyourbeets.interfaces.subscribers.OnLosingHPSubscriber;
import pinacolada.cards.base.CardSeries;
import pinacolada.powers.PCLCombatStats;
import pinacolada.relics.PCLRelic;
import pinacolada.resources.GR;
import pinacolada.rewards.pcl.ConcertsFinalHourReward;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

import java.util.ArrayList;

public class ConcertsFinalHour extends PCLRelic implements OnLosingHPSubscriber, OnReceiveRewardsListener
{
    public static final String ID = CreateFullID(ConcertsFinalHour.class);

    public ConcertsFinalHour()
    {
        super(ID, RelicTier.STARTER, LandingSound.MAGICAL);
        SetCounter(0);
    }

    @Override
    public void setCounter(int setCounter) {
        if (setCounter == -2) {
            this.usedUp();
            this.counter = -2;
        }
    }

    @Override
    public void atBattleStart()
    {
        super.atBattleStart();

        PCLCombatStats.onLosingHP.Subscribe(this);
    }

    @Override
    public int OnLosingHP(int damageAmount) {
        if (usedUp) {
            PCLCombatStats.onLosingHP.Unsubscribe(this);
        }
        else if (damageAmount > 0 && player.currentHealth <= damageAmount && PCLGameUtilities.InBattle())
        {
            SetCounter(-2);
            flash();
            PCLActions.Top.Add(new RelicAboveCreatureAction(player, this));
            PCLActions.Bottom.Heal(Math.max(1, player.maxHealth / 2));
            PCLCombatStats.onLosingHP.Unsubscribe(this);
            return 0;
        }
        return damageAmount;
    }

    @Override
    public void OnReceiveRewards(ArrayList<RewardItem> rewards, boolean b) {
        if (!usedUp && PCLGameUtilities.InBossRoom()) {
            rewards.add(new ConcertsFinalHourReward(GR.PCL.Dungeon.StartingSeries != null && GR.PCL.Dungeon.StartingSeries.Series != null ? GR.PCL.Dungeon.StartingSeries.Series : CardSeries.ANY));
            SetCounter(-2);
            flash();
        }
    }
}